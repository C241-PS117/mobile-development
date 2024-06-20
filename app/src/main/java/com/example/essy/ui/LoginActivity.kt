package com.example.essy.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.essy.R
import com.example.essy.view_model.LoginViewModel
import com.example.essy.databinding.ActivityLoginBinding
import com.example.essy.utils.ResultData

class LoginActivity : AppCompatActivity(), View.OnFocusChangeListener, View.OnKeyListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)

        if (isLoggedIn()) {
            goToMainActivity()
        }

        binding.etUsername.onFocusChangeListener = this
        binding.etPassword.onFocusChangeListener = this

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Silakan isi semua kolom", Toast.LENGTH_SHORT).show()
            } else if (password.length < 8) {
                Toast.makeText(this, "Kata sandi harus terdiri dari minimal 8 karakter", Toast.LENGTH_SHORT).show()
            } else {
                loginViewModel.login(username, password)
            }
        }

        binding.txtRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        loginViewModel.loginResult.observe(this, Observer { result ->
            when (result) {
                is ResultData.Loading -> {
                    showLoading(true)
                }
                is ResultData.Success -> {
                    showLoading(false)
                    val response = result.data
                    if (!response.error) {
                        saveUserCredentials(
                            response.data.username,
                            response.data.id.toString(),
                            response.data.urlgambar,
                            response.data.email,
                            response.data.jeniskelamin
                        )
                        goToMainActivity()
                    } else {
                        // Handle specific error messages
                        if (response.message == "Invalid username or password") {
                            Toast.makeText(this, "Nama pengguna atau kata sandi salah", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Gagal masuk. ${response.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                is ResultData.Error -> {
                    showLoading(false)
                    Log.e("LoginError", result.exception.message.toString())
                    Toast.makeText(this, "Gagal masuk. ${result.exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun saveUserCredentials(username: String?, userId: String?, urlgambar: String?, email: String?, jenisKelamin: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        editor.putString("user_id", userId)
        editor.putString("profile_image_url", urlgambar)
        editor.putString("profile_email", email)
        editor.putString("jenisKelamin", jenisKelamin)
        editor.putBoolean("is_logged_in", true)
        editor.apply()
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("is_logged_in", false)
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (view != null) {
            when(view.id) {
                R.id.et_username -> {
                    if (!hasFocus) {
                        validationUsername()
                    } else {
                        binding.layoutEtUsername.error = null
                    }
                }
                R.id.et_password -> {
                    if (!hasFocus) {
                        validationPassword()
                    } else {
                        binding.layoutEtPassword.error = null
                    }
                }
            }
        }
    }

    override fun onKey(view: View?, event: Int, keyEvent: KeyEvent?): Boolean {
        return false
    }

    private fun validationUsername(): Boolean {
        var errorMessage: String? = null
        val value = binding.etUsername.text.toString()

        if (value.isEmpty()) {
            errorMessage = "Username diperlukan"
        }

        if (errorMessage != null) {
            binding.layoutEtUsername.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        } else {
            binding.layoutEtUsername.error = null
        }

        return errorMessage == null
    }

    private fun validationPassword(): Boolean {
        var errorMessage: String? = null
        val value = binding.etPassword.text.toString()

        if (value.isEmpty()) {
            errorMessage = "Password diperlukan"
        } else if (value.length < 8) {
            errorMessage = "Password harus sepanjang 8 karakter"
        }

        if (errorMessage != null) {
            binding.layoutEtPassword.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        } else {
            binding.layoutEtPassword.error = null
        }

        return errorMessage == null
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
