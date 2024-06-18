package com.example.essy.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.essy.R
import com.example.essy.adapter.QuestionAdapter
import com.example.essy.data.model.QuestionResult
import com.example.essy.databinding.FragmentHomeBinding
import com.example.essy.utils.ResultData
import com.example.essy.view_model.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var questionAdapter: QuestionAdapter

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
        val userId = sharedPreferences.getString("user_id", null)?.toIntOrNull()
        if (userId != null) {
            viewModel.getQuestions(userId)
        }
        displayUserInfo()
        setupFabButtons()
        setupRecyclerView()
        observerViewModel()

    }

    private fun observerViewModel() {
        viewModel.questionResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is ResultData.Loading -> {
                    // Show loading indicator if needed
                }
                is ResultData.Success -> {
                    val questions = result.data.data // Assuming data is List<QuestionResult>
                    displayQuestionList(questions)
                }
                is ResultData.Error -> {
                    Toast.makeText(requireContext(), "Failed to fetch questions", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setupRecyclerView() {
        questionAdapter = QuestionAdapter(emptyList()) { question ->
            // Handle item click here
            navigateToScanActivity(question)
        }
        binding.recylerViewMain.apply {
            adapter = questionAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun displayQuestionList(questions: List<QuestionResult>) {
        questionAdapter = QuestionAdapter(questions) { question ->
            // Handle item click here
            navigateToScanActivity(question)
        }
        binding.recylerViewMain.adapter = questionAdapter
        binding.countKeyword.text = questions.size.toString()
    }

    private fun navigateToScanActivity(question: QuestionResult) {
        val intent = Intent(activity, ScanActivity::class.java).apply {
            putExtra("QUESTION_ID", question.id)
            putExtra("QUESTION_ANSWER", question.jawaban)
        }
        startActivity(intent)
    }

    private fun displayUserInfo() {
        val username = sharedPreferences.getString("username", "User")
        val profileImageUrl = sharedPreferences.getString("profile_image_url", null)

        binding.hometext1.text = getString(R.string.welcome_message, username)

        Log.d("HomeFragment", "Profile image URL: $profileImageUrl") // Add this line

        if (profileImageUrl != null) {
            binding.circleProfile.load(profileImageUrl) {
                transformations(CircleCropTransformation())
            }
        } else {
            Log.d("HomeFragment", "Profile image URL is null")
        }
    }

    private fun setupFabButtons() {

        binding.fabAdd.setOnClickListener {
            val intent = Intent(activity, AddKeywordActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
