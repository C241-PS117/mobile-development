package com.example.essy.ui.home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.essy.R
import com.example.essy.databinding.ActivityAddKeywordBinding

class AddKeywordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddKeywordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_keyword)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_keyword)

        initUi()
    }

    private fun initUi(){
        binding.customToolbar.btnBack.setOnClickListener {
            finish()
        }
        binding.customToolbar.txtTitle.text = "Tambah Soal dan Keyword"
    }
}