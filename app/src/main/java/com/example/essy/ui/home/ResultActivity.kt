package com.example.essy.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.essy.data.network.ApiConfig
import com.example.essy.databinding.ActivityResultBinding
import com.example.essy.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        // Get total score from intent
        val totalNilaiJawaban = intent.getIntExtra("TOTAL_NILAI_JAWABAN", 0)
        binding.countScore.text = totalNilaiJawaban.toString()

        // Get question ID from intent
        val idSoal = intent.getIntExtra("QUESTION_ID", 0)

        binding.btnSimpan.setOnClickListener {
            // Save data to API when save button is clicked
            saveData(idSoal, totalNilaiJawaban)
        }
    }

    private fun saveData(idSoal: Int, totalNilaiJawaban: Int) {
        val nama = binding.etUsername.text.toString().trim() // Get value from EditText
        val idGuru = sharedPreferences.getString("user_id", null) // Get teacher ID from SharedPreferences

        // Validate input
        if (nama.isEmpty() || idGuru.isNullOrEmpty()) {
            Toast.makeText(this, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        // Call API to save data
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiConfig.getApiService().tambahNilai(
                    idSoal.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    idGuru!!.toRequestBody("text/plain".toMediaTypeOrNull()), // !! operator to indicate that idGuru is not null
                    nama.toRequestBody("text/plain".toMediaTypeOrNull()),
                    totalNilaiJawaban.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                )
                withContext(Dispatchers.Main) {
                    if (response.message == "Data added successfully") {
                        Toast.makeText(this@ResultActivity, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
                        val i = Intent(this@ResultActivity, MainActivity::class.java)
                        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(i)
                    } else {
                        Toast.makeText(this@ResultActivity, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("ResultActivity", "Error: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ResultActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
