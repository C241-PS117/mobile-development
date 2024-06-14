package com.example.essy.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.essy.data.model.LoginResponse
import com.example.essy.data.network.ApiConfig
import com.example.essy.utils.ResultData
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class LoginViewModel : ViewModel() {
    val loginResult = MutableLiveData<ResultData<LoginResponse>>()

    fun login(username: String, password: String) {
        loginResult.value = ResultData.Loading

        // Convert strings to RequestBody
        val usernameRequestBody = username.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val passwordRequestBody = password.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        viewModelScope.launch {
            try {
                // Pass RequestBody instances to the API call
                val response = ApiConfig.getApiService().login(usernameRequestBody, passwordRequestBody)
                loginResult.value = ResultData.Success(response)
            } catch (e: Exception) {
                loginResult.value = ResultData.Error(e)
            }
        }
    }
}
