package com.example.essy.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.essy.data.model.QuestionResponse
import com.example.essy.data.network.ApiConfig
import com.example.essy.utils.ResultData
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class HomeViewModel : ViewModel() {

    val questionResult = MutableLiveData<ResultData<QuestionResponse>>()

    fun getQuestions(id: Int) {
        questionResult.value = ResultData.Loading

        // Convert ID to RequestBody
        val idRequestBody = id.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

        viewModelScope.launch {
            try {
                // Call getQuestions API
                val response = ApiConfig.getApiService().getSoal(idRequestBody)
                questionResult.value = ResultData.Success(response)
            } catch (e: Exception) {
                questionResult.value = ResultData.Error(e)
            }
        }
    }
}