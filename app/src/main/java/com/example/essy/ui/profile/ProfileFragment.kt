package com.example.essy.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.example.essy.R
import com.example.essy.databinding.FragmentProfileBinding
import com.example.essy.ui.LoginActivity

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUserData()
        buttonListener()
    }

    private fun loadUserData() {
        val sharedPreferences = activity?.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val username = sharedPreferences?.getString("username", "Username")
        val email = sharedPreferences?.getString("profile_email", "Email")
        val profileImageUrl = sharedPreferences?.getString("profile_image_url", "")

        binding.profileName.text = username
        binding.profileEmail.text = email

        binding.circleProfile.load(profileImageUrl) {
        }
    }

    private fun buttonListener() {
        binding.btnEdit.setOnClickListener {
            val intent = Intent(activity, EditActivity::class.java)
            startActivity(intent)
        }
        binding.btnPassword.setOnClickListener {
            val intent = Intent(activity, PasswordActivity::class.java)
            startActivity(intent)
        }
        binding.btnInformasi.setOnClickListener {
            val intent = Intent(activity, InformationActivity::class.java)
            startActivity(intent)
        }
        binding.btnTeam.setOnClickListener {
            val intent = Intent(activity, OurTeamActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogout.setOnClickListener {
            clearUserSession()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun clearUserSession() {
        val sharedPreferences = activity?.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences?.edit()?.clear()?.apply()
    }
}