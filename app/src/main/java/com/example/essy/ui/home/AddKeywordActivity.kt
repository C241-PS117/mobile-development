package com.example.essy.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.essy.R
import com.example.essy.data.network.ApiConfig
import com.example.essy.databinding.ActivityAddKeywordBinding
import com.example.essy.ui.MainActivity
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
        binding.customToolbar.txtTitle.text = "Tambah Soal dan Jawaban"

        binding.btnSimpan.setOnClickListener {
            val soal = binding.etUsername.text.toString().trim()
            val jawaban = binding.etDescription.text.toString().trim()

            if (soal.isEmpty() || jawaban.isEmpty()) {
                Toast.makeText(this, "Silakan isi semua kolom", Toast.LENGTH_SHORT).show()
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
                try {
                    val response = ApiConfig.getApiService().tambahSoal(idGuruBody, soalBody, jawabanBody)
                    withContext(Dispatchers.Main) {
                        if (response != null && response.message == "Soal dan jawaban berhasil ditambahkan") {
                            Toast.makeText(this@AddKeywordActivity, "Soal dan jawaban berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                            val i = Intent(this@AddKeywordActivity, MainActivity::class.java)
                            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(i)
                            finish()
                        } else {
                            Toast.makeText(this@AddKeywordActivity, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("AddKeywordActivity", "Error: ${e.message}", e)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@AddKeywordActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(this, "ID pengguna tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }

}
