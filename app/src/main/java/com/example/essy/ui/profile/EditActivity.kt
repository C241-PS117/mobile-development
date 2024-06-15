package com.example.essy.ui.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import coil.load
import com.example.essy.R
import com.example.essy.data.network.ApiConfig
import com.example.essy.data.network.ApiService
import com.example.essy.databinding.ActivityEditBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit)

        apiService = ApiConfig.getApiService() // Inisialisasi ApiService menggunakan ApiConfig
        initUi()
        loadUserData()
        setupSaveButton()
    }

    private fun initUi() {
        binding.customToolbar.btnBack.setOnClickListener {
            finish()
        }
        binding.customToolbar.txtTitle.text = "Edit Profil"
    }

    private fun loadUserData() {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")
        val email = sharedPreferences.getString("profile_email", "")
        val jenisKelamin = sharedPreferences.getString("jenisKelamin", "")
        val profileImageUrl = sharedPreferences.getString("profile_image_url", "")

        binding.etNamaPengguna.setText(username)
        binding.etEmail.setText(email)
        binding.etJenisKelamin.setText(jenisKelamin)
        binding.circleProfile.load(profileImageUrl) {
        }
    }

private fun setupSaveButton() {
    binding.btnSimpan.setOnClickListener {
        val username = binding.etNamaPengguna.text.toString()
        val email = binding.etEmail.text.toString()
        val jenisKelamin = binding.etJenisKelamin.text.toString()
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val profileImageUrl = sharedPreferences.getString("profile_image_url", "")

        // Konversi nilai string ke RequestBody
        val usernameRequestBody = username.toRequestBody("text/plain".toMediaTypeOrNull())
        val emailRequestBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val jenisKelaminRequestBody = jenisKelamin.toRequestBody("text/plain".toMediaTypeOrNull())
        val profileImageRequestBody = profileImageUrl?.toRequestBody("text/plain".toMediaTypeOrNull())

        Log.d("SetupSaveButton", "Username: $username, Email: $email, Jenis Kelamin: $jenisKelamin, Profile Image URL: $profileImageUrl")

        // Panggil fungsi updateProfile
        updateProfile(usernameRequestBody, emailRequestBody, jenisKelaminRequestBody, profileImageRequestBody) { success ->
            if (success) {
                // Simpan nilai asli ke SharedPreferences jika berhasil
                saveUserDataToPreferences(username, email, jenisKelamin, profileImageUrl ?: "")
            }
        }
    }
}


private fun updateProfile(
    username: RequestBody,
    email: RequestBody,
    jenisKelamin: RequestBody,
    profileImage: RequestBody?,
    callback: (Boolean) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = apiService.editprofil(username, email, jenisKelamin, profileImage)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@EditActivity, response.message, Toast.LENGTH_SHORT).show()
                Log.d("UpdateProfile", "Response: $response")
                callback(true)
            }
        } catch (e: HttpException) {
            withContext(Dispatchers.Main) {
                if (e.code() == 400) {
                    Toast.makeText(this@EditActivity, "Bad Request: ${e.response()?.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@EditActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                Log.e("UpdateProfile", "HttpException: ${e.response()?.errorBody()?.string()}")
                callback(false)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@EditActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("UpdateProfile", "Exception: ${e.message}")
                callback(false)
            }
        }
    }
}

    private fun saveUserDataToPreferences(username: String, email: String, jenisKelamin: String, profileImageUrl: String) {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("username", username)
            putString("profile_email", email)
            putString("jenisKelamin", jenisKelamin)
            putString("profile_image_url", profileImageUrl)
            apply()
        }
    }
}
