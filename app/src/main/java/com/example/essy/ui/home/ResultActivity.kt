package com.example.essy.ui.home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.essy.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val totalNilaiJawaban = intent.getIntExtra("TOTAL_NILAI_JAWABAN", 0)
        binding.countScore.text = totalNilaiJawaban.toString()

        binding.btnSimpan.setOnClickListener {
            finish()
        }
    }
}
