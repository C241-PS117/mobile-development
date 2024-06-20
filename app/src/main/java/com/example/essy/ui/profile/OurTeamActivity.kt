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
                "Mobile Development",
                R.drawable.team_krisna,
                "https://linkedin.com/in/khrisnandika",
                "https://instagram.com/khrisnandika"
            ),
            TeamMember(
                "Indra Prasetyo Wijaya ",
                "Mobile Development",
                R.drawable.team_indra,
                "https://linkedin.com/in/indra-prasetyo-wijaya",
                "https://instagram.com/indrapraass"
            ),
            TeamMember(
                "Melanie Sayyidina Sabrina Refman",
                "Machine Learning",
                R.drawable.team_melanie,
                "https://linkedin.com/in/melanierefman",
                "https://instagram.com/melanierefman"
            ),
            TeamMember(
                "Yusna Millaturrosyidah",
                "Machine Learning",
                R.drawable.team_yusni,
                "https://linkedin.com/in/yusnamillaturrosyidah",
                "https://instagram.com/_yusnaa_"
            ),
            TeamMember(
                "Tsabita Putri Ramadhany",
                "Machine Learning",
                R.drawable.team_tsabiti,
                "https://linkedin.com/in/tsabita-putri-ramadhany",
                "https://instagram.com/tsabitaaptr"
            ),
            TeamMember(
                "Moch.Rikza Lucky Ardiansyah",
                "Cloud Computing",
                R.drawable.team_lucky,
                "https://linkedin.com/in/moch-rikza-lucky-ardiansyah-87009b220/",
                "https://instagram.com/rikzalucky_a"
            ),
            TeamMember(
                "Muhammad Daffa Naufal Muzaki ",
                "Cloud Computing",
                R.drawable.team_daffa,
                "https://linkedin.com/in/muhammad-daffa-naufal-muzaki-b70592293?utm_source=share&utm_campaign=share_via&utm_content=profile&utm_medium=android_app",
                "https://instagram.com/problemacy"
            ),
        )
    }
}