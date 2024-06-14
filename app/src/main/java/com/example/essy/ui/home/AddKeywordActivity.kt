package com.example.essy.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.essy.R
import com.example.essy.data.network.ApiConfig
import com.example.essy.databinding.ActivityAddKeywordBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddKeywordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddKeywordBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_keyword)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_keyword)

        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        initUi()
    }

    private fun initUi() {
        binding.customToolbar.btnBack.setOnClickListener {
            finish()
        }
        binding.customToolbar.txtTitle.text = "Tambah Soal dan Keyword"

        binding.btnSimpan.setOnClickListener {
            val soal = binding.etUsername.text.toString().trim()
            val jawaban = binding.etDescription.text.toString().trim()

            if (soal.isEmpty() || jawaban.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                tambahSoal(soal, jawaban)
            }
        }
    }

    private fun tambahSoal(soal: String, jawaban: String) {
        val idGuru = sharedPreferences.getString("user_id", null)
        if (idGuru != null) {
            val idGuruBody = idGuru.toRequestBody("text/plain".toMediaTypeOrNull())
            val soalBody = soal.toRequestBody("text/plain".toMediaTypeOrNull())
            val jawabanBody = jawaban.toRequestBody("text/plain".toMediaTypeOrNull())

            CoroutineScope(Dispatchers.IO).launch {
                val response = ApiConfig.getApiService().tambahSoal(idGuruBody, soalBody, jawabanBody)
                withContext(Dispatchers.Main) {
                    if (response != null) {
                        Toast.makeText(this@AddKeywordActivity, response.message, Toast.LENGTH_SHORT).show()
                        if (response.message == "Success") {
                            finish()
                        }
                    } else {
                        Toast.makeText(this@AddKeywordActivity, "Failed to add question", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
        }
    }
}
