package com.example.essy.ui.profile

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
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
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private lateinit var sharedPreferences: SharedPreferences

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
        val idBody = userId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val usernameBody = username.toRequestBody("text/plain".toMediaTypeOrNull())
        val emailBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val jenisKelaminBody = jenisKelamin.toRequestBody("text/plain".toMediaTypeOrNull())
        val profileImageBody = profileImageUrl?.toRequestBody("text/plain".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiConfig.getApiService().editprofil(idBody, usernameBody, emailBody, jenisKelaminBody, profileImageBody)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@EditActivity, response.message, Toast.LENGTH_SHORT).show()
                    if (response.message == "Profile updated successfully") {
                        saveUserDataToPreferences(username, email, jenisKelamin, profileImageUrl ?: "")
                        setResult(Activity.RESULT_OK) // Set result untuk menandakan berhasil
                        finish() // Kembali ke halaman sebelumnya
                    }
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    if (e.code() == 400) {
                        Toast.makeText(this@EditActivity, "Bad Request: ${e.response()?.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@EditActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@EditActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
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
}
