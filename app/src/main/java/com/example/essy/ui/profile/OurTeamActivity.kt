package com.example.essy.ui.profile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.essy.R
import com.example.essy.adapter.TeamAdapter
import com.example.essy.data.model.TeamMember
import com.example.essy.databinding.ActivityOurTeamBinding

class OurTeamActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOurTeamBinding
    private lateinit var adapter: TeamAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_our_team)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_our_team)

        initUi()

        // Setup RecyclerView
        adapter = TeamAdapter(getTeamMembers())
        binding.recylerViewMain.layoutManager = LinearLayoutManager(this)
        binding.recylerViewMain.adapter = adapter
    }
    private fun initUi(){
        binding.customToolbar.btnBack.setOnClickListener {
            finish()
        }
        binding.customToolbar.txtTitle.text = "Tim Kami"
    }
    private fun getTeamMembers(): List<TeamMember> {
        return listOf(
            TeamMember(
                "Krisna Choiril Andika",
                "khrisnandika15@gmail.com",
                R.drawable.team_krisna,
                "https://linkedin.com/in/khrisnandika",
                "https://instagram.com/khrisnandika"
            ),
            TeamMember(
                "Indra Prasetyo Wijaya ",
                "indrapras2002@gmail.com",
                R.drawable.team_indra,
                "https://www.linkedin.com/in/indra-prasetyo-wijaya/",
                "https://instagram.com/indrapraass"
            ),
            TeamMember(
                "Melanie Sayyidina Sabrina Refman",
                "melanie@gmail.com",
                R.drawable.team_krisna,
                "https://linkedin.com/in/khrisnandika",
                "https://instagram.com/khrisnandika"
            ),
            TeamMember(
                "Yusna Millaturrosyidah",
                "yusna@gmail.com",
                R.drawable.team_krisna,
                "https://linkedin.com/in/khrisnandika",
                "https://instagram.com/khrisnandika"
            ),
            TeamMember(
                "Tsabita Putri Ramadhan",
                "tsabita@gmail.com",
                R.drawable.team_krisna,
                "https://linkedin.com/in/khrisnandika",
                "https://instagram.com/khrisnandika"
            ),
            TeamMember(
                "Moch.Rikza Lucky Ardiansyah",
                "lucky@gmail.com",
                R.drawable.team_krisna,
                "https://linkedin.com/in/khrisnandika",
                "https://instagram.com/khrisnandika"
            ),
            TeamMember(
                "Muhammad Daffa Naufal Muzaki ",
                "dafa@gmail.com",
                R.drawable.team_krisna,
                "https://linkedin.com/in/khrisnandika",
                "https://instagram.com/khrisnandika"
            ),
        )
    }
}