package com.example.essy.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.essy.R
import com.example.essy.data.model.LoginResponse
import com.example.essy.data.network.ApiConfig
import com.example.essy.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity(), View.OnFocusChangeListener, View.OnKeyListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)

        if (isLoggedIn()) {
            navigateToMainActivity()
        }

        setupUI()

        binding.etUsername.onFocusChangeListener = this
        binding.etPassword.onFocusChangeListener = this

        binding.btnLogin.setOnClickListener {
            handleLoginButtonClick()
        }

        binding.txtRegister.setOnClickListener {
            navigateToRegisterScreen()
        }
    }

    private fun setupUI() {
        binding.apply {
            layoutEtUsername.visibility = View.VISIBLE
            layoutEtPassword.visibility = View.VISIBLE
            btnLogin.visibility = View.VISIBLE
            txtTitle.visibility = View.VISIBLE
            txtRegister.visibility = View.VISIBLE
        }
    }



    private fun handleLoginButtonClick() {
        val email = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        } else {
            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiConfig.getApiService().login(email, password)
                withContext(Dispatchers.Main) {
                    handleLoginResponse(response)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Login failed. Please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun handleLoginResponse(response: LoginResponse) {
        if (!response.error) {
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
            saveUserCredentials(response.data?.username, response.data?.email)
            navigateToMainActivity()
        } else {
            Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("is_logged_in", false)
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        view?.let {
            when (it.id) {
                R.id.et_username -> if (!hasFocus) validateEmail() else binding.layoutEtUsername.error =
                    null

                R.id.et_password -> if (!hasFocus) validatePassword() else binding.layoutEtPassword.error =
                    null

                else -> {}
            }
        }
    }

    override fun onKey(view: View?, keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }

    private fun validateEmail(): Boolean {
        val email = binding.etUsername.text.toString()
        val errorMessage = when {
            email.isEmpty() -> "Email is required"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Invalid email address"
            else -> null
        }

        binding.etUsername.apply {
            isErrorEnabled = errorMessage != null
            error = errorMessage
        }

        return errorMessage == null
    }

    private fun validatePassword(): Boolean {
        val password = binding.etPassword.text.toString()
        val errorMessage = when {
            password.isEmpty() -> "Password is required"
            password.length < 8 -> "Password must be at least 8 characters long"
            else -> null
        }

        binding.etPassword.apply {
            isErrorEnabled = errorMessage != null
            error = errorMessage
        }

        return errorMessage == null
    }

    private fun saveUserCredentials(username: String?, token: String?) {
        sharedPreferences.edit().apply {
            putString("username", username)
            putString("token", token)
            putBoolean("is_logged_in", true)
            apply()
        }
    }

    private fun navigateToRegisterScreen() {
        startActivity(Intent(this, RegisterActivity::class.java))
        finish()
    }
}
