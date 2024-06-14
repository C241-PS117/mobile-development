package com.example.essy.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.essy.data.model.RegisterRequest
import com.example.essy.data.model.RegisterResponse
import com.example.essy.data.network.ApiConfig
import com.example.essy.utils.ResultData
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    val registerResult = MutableLiveData<ResultData<RegisterResponse>>()

    fun register(request: RegisterRequest) {
        registerResult.value = ResultData.Loading

        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().register(request)
                registerResult.value = ResultData.Success(response)
            } catch (e: Exception) {
                registerResult.value = ResultData.Error(e)
            }
        }
    }
}
