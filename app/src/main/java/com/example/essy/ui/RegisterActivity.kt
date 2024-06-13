package com.example.essy.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.essy.data.model.RegisterRequest
import com.example.essy.data.model.RegisterResponse
import com.example.essy.data.network.ApiConfig
import com.example.essy.databinding.ActivityRegisterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val jenisKelamin = "Laki-laki"
            val dataGambar = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.britannica.com%2Fmoney%2FMark-Zuckerberg&psig=AOvVaw3cftcRG5BPXFsNqCWT1zCA&ust=1718279937921000&source=images&cd=vfe&opi=89978449&ved=0CBIQjRxqFwoTCIjS_duB1oYDFQAAAAAdAAAAABAE" // Sesuaikan dengan URL gambar

            if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val registerRequest = RegisterRequest(username, password, email, jenisKelamin, dataGambar)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = ApiConfig.getApiService().register(registerRequest)
                    withContext(Dispatchers.Main) {
                        handleRegisterResponse(response)
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@RegisterActivity, "Register failed. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.txtLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun handleRegisterResponse(response: RegisterResponse) {
        Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
        // Jika registrasi berhasil, pindahkan pengguna ke halaman login
        if (response.message == "Registration successful") {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
