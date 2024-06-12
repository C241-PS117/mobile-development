package com.example.essy.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.essy.R
import com.example.essy.databinding.ActivityScanBinding

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_scan)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scan)

        initUi()
    }
    private fun initUi(){
        binding.customToolbar.btnBack.setOnClickListener {
            finish()
        }
        binding.customToolbar.txtTitle.text = "Scan Jawaban"
        binding.btnUpload.setOnClickListener {
            val intent = Intent(this, ResultActivity::class.java)
            startActivity(intent)
        }
    }
}