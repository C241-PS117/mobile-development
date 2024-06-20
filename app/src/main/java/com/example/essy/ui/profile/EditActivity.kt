package com.example.essy.ui.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import coil.load
import com.example.essy.R
import com.example.essy.data.network.ApiConfig
import com.example.essy.databinding.ActivityEditBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var photoFile: File

    private val startForResultGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val selectedImage: Uri? = data?.data
            selectedImage?.let {
                binding.circleProfile.setImageURI(it)
                photoFile = uriToFile(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit)

        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        initUi()
        loadUserData()
    }

    private fun initUi() {
        binding.customToolbar.btnBack.setOnClickListener {
            finish()
        }
        binding.customToolbar.txtTitle.text = "Edit Profil"

        binding.editProfile.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startForResultGallery.launch(galleryIntent)
        }

        binding.btnSimpan.setOnClickListener {
            val username = binding.etNamaPengguna.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val jenisKelamin = binding.etJenisKelamin.text.toString().trim()
            val profileImageUrl = sharedPreferences.getString("profile_image_url", "")

            val userId = sharedPreferences.getString("user_id", null)?.toIntOrNull()
            if (userId != null) {
                ubahProfile(userId, username, email, jenisKelamin, profileImageUrl)
            } else {
                Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun ubahProfile(userId: Int, username: String, email: String, jenisKelamin: String, profileImageUrl: String?) {
        // Menampilkan ProgressBar saat mengirim data
        binding.progressBar.visibility = View.VISIBLE

        val idBody = userId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val usernameBody = username.toRequestBody("text/plain".toMediaTypeOrNull())
        val emailBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val jenisKelaminBody = jenisKelamin.toRequestBody("text/plain".toMediaTypeOrNull())

        // Check if photoFile is not null and create RequestBody for it
        val photoRequestBody = if (::photoFile.isInitialized) {
            val requestFile = photoFile.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("DataGambar", photoFile.name, requestFile)
        } else {
            null
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiConfig.getApiService().editprofil(idBody, usernameBody, emailBody, jenisKelaminBody, photoRequestBody)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@EditActivity, response.message, Toast.LENGTH_SHORT).show()
                    if (response.message == "Profil berhasil diperbarui") {
                        saveUserDataToPreferences(username, email, jenisKelamin, profileImageUrl ?: "")
                        setResult(Activity.RESULT_OK) // Set result untuk menandakan berhasil
                        finish() // Kembali ke halaman sebelumnya
                    }
                    // Sembunyikan ProgressBar setelah selesai
                    binding.progressBar.visibility = View.GONE
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    if (e.code() == 400) {
                        Toast.makeText(this@EditActivity, "Bad Request: ${e.response()?.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@EditActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                    // Sembunyikan ProgressBar setelah selesai
                    binding.progressBar.visibility = View.GONE
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@EditActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    // Sembunyikan ProgressBar setelah selesai
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }



    private fun loadUserData() {
        val username = sharedPreferences.getString("username", "")
        val email = sharedPreferences.getString("profile_email", "")
        val jenisKelamin = sharedPreferences.getString("jenisKelamin", "")
        val profileImageUrl = sharedPreferences.getString("profile_image_url", "")

        binding.etNamaPengguna.setText(username)
        binding.etEmail.setText(email)
        binding.etJenisKelamin.setText(jenisKelamin)
        binding.circleProfile.load(profileImageUrl) {
            // Handle image loading options if needed
        }
    }

    private fun saveUserDataToPreferences(username: String, email: String, jenisKelamin: String, profileImageUrl: String) {
        with(sharedPreferences.edit()) {
            putString("username", username)
            putString("profile_email", email)
            putString("jenisKelamin", jenisKelamin)
            putString("profile_image_url", profileImageUrl)
            apply()
        }
    }

    private fun uriToFile(uri: Uri): File {
        val contentResolver = contentResolver
        val myFile = File.createTempFile("temp_image", null, cacheDir)
        contentResolver.openInputStream(uri)?.use { inputStream ->
            myFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return myFile
    }
}
