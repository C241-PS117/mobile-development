package com.example.essy.ui.home

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.essy.R
import com.example.essy.adapter.NilaiAdapter
import com.example.essy.data.model.NilaiResponse
import com.example.essy.data.model.NilaiResult
import com.example.essy.data.network.ApiConfig
import com.example.essy.databinding.ActivityHistoryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var nilaiAdapter: NilaiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_history)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history)

        initUi()

        setupRecyclerView()

        // Mendapatkan ID Guru dari Shared Preferences
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val idGuru = sharedPreferences.getString("user_id", null)?.toIntOrNull()

        if (idGuru != null) {
            loadNilai(idGuru)
        } else {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        nilaiAdapter = NilaiAdapter(emptyList())
        binding.recylerViewScore.apply {
            adapter = nilaiAdapter
            layoutManager = LinearLayoutManager(this@HistoryActivity)
            setHasFixedSize(true)
        }
    }

    private fun loadNilai(idGuru: Int) {
        val apiService = ApiConfig.getApiService()

        val idGuruRequestBody = idGuru.toString().toRequestBody(okhttp3.MultipartBody.FORM)

        // Start a coroutine to make the network request
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Call the suspend function directly
                val response = apiService.getAllNilai(idGuruRequestBody)

                // Switch to the main thread to update UI
                withContext(Dispatchers.Main) {
                    if (response.status == "success") {
                        val nilaiList = response.data
                        nilaiAdapter.setData(nilaiList)
                    } else {
                        Toast.makeText(this@HistoryActivity, "Failed to get data: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@HistoryActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun initUi(){
        binding.customToolbar.btnBack.setOnClickListener {
            finish()
        }
        binding.customToolbar.txtTitle.text = "Histori Nilai Siswa"
    }
}
