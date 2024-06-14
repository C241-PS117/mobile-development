package com.example.essy.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.essy.data.model.RegisterRequest
import com.example.essy.data.model.RegisterResponse
import com.example.essy.databinding.ActivityRegisterBinding
import com.example.essy.utils.ResultData
import com.example.essy.view_model.RegisterViewModel
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmpassword.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val jenisKelamin = ""
            val dataGambar = ""

            if (validateForm(username, email, password, confirmPassword)) {
                val registerRequest = RegisterRequest(username, password, email, jenisKelamin, dataGambar)
                Log.d("RegisterActivity", "Sending register request: $registerRequest")
                registerViewModel.register(registerRequest)
            } else {
                Toast.makeText(this, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
            }
        }

        binding.txtLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        registerViewModel.registerResult.observe(this, Observer { result ->
            Log.d("RegisterActivity", "Observer triggered with result: $result")
            when (result) {
                is ResultData.Loading -> {
                    Log.d("RegisterActivity", "Register request is loading")
                }
                is ResultData.Success -> {
                    handleRegisterResponse(result.data)
                }
                is ResultData.Error -> {
                    Log.e("RegisterActivity", "Register failed", result.exception)
                    Toast.makeText(this, "Register failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun validateForm(username: String, email: String, password: String, confirmPassword: String): Boolean {
        var isValid = true

        if (username.isEmpty()) {
            binding.etUsername.error = "Username is required"
            isValid = false
        }
        if (email.isEmpty()) {
            binding.etEmail.error = "Email is required"
            isValid = false
        } else if (!isValidEmail(email)) {
            binding.etEmail.error = "Invalid email address"
            isValid = false
        }
        if (password.isEmpty()) {
            binding.etPassword.error = "Password is required"
            isValid = false
        } else if (password.length < 8) {
            binding.etPassword.error = "Password must be at least 8 characters long"
            isValid = false
        }
        if (confirmPassword.isEmpty()) {
            binding.etConfirmpassword.error = "Confirm Password is required"
            isValid = false
        } else if (password != confirmPassword) {
            binding.etConfirmpassword.error = "Passwords do not match"
            isValid = false
        }

        return isValid
    }

    private fun isValidEmail(email: String): Boolean {
        val pattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
        return pattern.matcher(email).matches()
    }

    private fun handleRegisterResponse(response: RegisterResponse) {
        Log.d("RegisterActivity", "Register response: $response")
        Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
        if (response.message.contains("success", ignoreCase = true)) {
            Log.d("RegisterActivity", "Registration successful, navigating to LoginActivity")
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            Log.d("RegisterActivity", "Registration failed with message: ${response.message}")
        }
    }
}
