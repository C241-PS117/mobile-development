package com.example.essy.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.essy.data.model.LoginResponse
import com.example.essy.data.network.ApiConfig
import com.example.essy.utils.ResultData
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    val loginResult = MutableLiveData<ResultData<LoginResponse>>()

    fun login(email: String, password: String) {
        loginResult.value = ResultData.Loading
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().login(email, password)
                loginResult.value = ResultData.Success(response)
            } catch (e: Exception) {
                loginResult.value = ResultData.Error(e)
            }
        }
    }
}
