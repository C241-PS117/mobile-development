package com.example.essy.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.example.essy.R
import com.example.essy.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var isFabOpen = false
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        displayUserInfo()

        binding.fabMain.setOnClickListener {
            if (isFabOpen) {
                closeFabMenu()
            } else {
                openFabMenu()
            }
        }

        binding.fabScan.setOnClickListener {
            val intent = Intent(activity, ScanActivity::class.java)
            startActivity(intent)
        }

        binding.fabAdd.setOnClickListener {
            val intent = Intent(activity, AddKeywordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun displayUserInfo() {
        val username = sharedPreferences.getString("username", "User")
        val profileImageUrl = sharedPreferences.getString("profile_image_url", null)

        binding.hometext1.text = getString(R.string.welcome_message, username)

        if (profileImageUrl != null) {
            Log.d("HomeFragment", "Profile image URL: $profileImageUrl")
            binding.circleProfile.load(profileImageUrl) {
                crossfade(true)
//                placeholder(R.drawable.placeholder_image)
//                error(R.drawable.error_image)
            }
        } else {
            Log.d("HomeFragment", "Profile image URL is null")
        }
    }

    private fun openFabMenu() {
        isFabOpen = true
        binding.fabScan.apply {
            visibility = View.VISIBLE
            animate().translationY(-resources.getDimension(R.dimen.fab_margin_1)).alpha(1f).setDuration(200).start()
        }
        binding.fabAdd.apply {
            visibility = View.VISIBLE
            animate().translationY(-resources.getDimension(R.dimen.fab_margin_2)).alpha(1f).setDuration(200).start()
        }
    }

    private fun closeFabMenu() {
        isFabOpen = false
        binding.fabScan.apply {
            animate().translationY(0f).alpha(0f).setDuration(200).withEndAction { visibility = View.GONE }.start()
        }
        binding.fabAdd.apply {
            animate().translationY(0f).alpha(0f).setDuration(200).withEndAction { visibility = View.GONE }.start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
