package com.example.essy.ui.profile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.essy.R
import com.example.essy.databinding.ActivityOurTeamBinding

class OurTeamActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOurTeamBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_our_team)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_our_team)

        initUi()
    }
    private fun initUi(){
        binding.customToolbar.btnBack.setOnClickListener {
            finish()
        }
        binding.customToolbar.txtTitle.text = "Tim Kami"
    }
}