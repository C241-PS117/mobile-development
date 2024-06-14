package com.example.essy.ui.profile

import android.os.Bundle
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import coil.load
import com.example.essy.R
import com.example.essy.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit)

        initUi()
        loadUserData()
    }

    private fun initUi(){
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
}
