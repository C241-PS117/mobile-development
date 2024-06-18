package com.example.essy.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.essy.data.model.CountJawabanResponse
import com.example.essy.data.model.QuestionResponse
import com.example.essy.data.network.ApiConfig
import com.example.essy.utils.ResultData
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class HomeViewModel : ViewModel() {

    val questionResult = MutableLiveData<ResultData<QuestionResponse>>()
    val countJawabanResult = MutableLiveData<ResultData<CountJawabanResponse>>()

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

    fun getCountJawaban(userId: Int) {
        countJawabanResult.value = ResultData.Loading

        val userIdRequestBody = userId.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getCountJawaban(userIdRequestBody)
                countJawabanResult.value = ResultData.Success(response)
            } catch (e: Exception) {
                countJawabanResult.value = ResultData.Error(e)
            }
        }
    }

}
