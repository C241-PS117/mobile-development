package com.example.essy.ui.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.essy.R
import com.example.essy.data.network.ApiConfig
import com.example.essy.databinding.ActivityPasswordBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class PasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPasswordBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_password)

        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        initUi()
    }

    private fun initUi() {
        binding.customToolbar.btnBack.setOnClickListener {
            finish()
        }
        binding.customToolbar.txtTitle.text = "Ubah Kata Sandi"

        binding.btnSimpan.setOnClickListener {
            val oldPassword = binding.etPasswordLama.text.toString().trim()
            val newPassword = binding.etPasswordBaru.text.toString().trim()
            val confirmPassword = binding.etKonfirmasiPasswordBaru.text.toString().trim()

            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else if (newPassword != confirmPassword) {
                Toast.makeText(this, "New Password and Confirm Password do not match", Toast.LENGTH_SHORT).show()
            } else {
                val userId = sharedPreferences.getString("user_id", null)?.toIntOrNull()
                if (userId != null) {
                    ubahPassword(userId, oldPassword, newPassword)
                } else {
                    Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun ubahPassword(userId: Int, oldPassword: String, newPassword: String) {
        val idBody = userId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val oldPasswordBody = oldPassword.toRequestBody("text/plain".toMediaTypeOrNull())
        val newPasswordBody = newPassword.toRequestBody("text/plain".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            val response = ApiConfig.getApiService().ubahPassword(idBody, oldPasswordBody, newPasswordBody)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@PasswordActivity, response.message, Toast.LENGTH_SHORT).show()
                if (response.message == "Password updated successfully") {
                    finish()
                } else {
                    Toast.makeText(this@PasswordActivity, "Failed to update password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
