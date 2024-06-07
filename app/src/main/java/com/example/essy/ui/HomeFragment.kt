package com.example.essy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.essy.R
import com.example.essy.databinding.FragmentHomeBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var isFabOpen = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabMain.setOnClickListener {
            if (isFabOpen) {
                closeFabMenu()
            } else {
                openFabMenu()
            }
        }

        binding.fabScan.setOnClickListener {
            // Handle scan button click
        }

        binding.fabAdd.setOnClickListener {
            // Handle add button click
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
