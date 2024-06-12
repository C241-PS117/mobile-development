package com.example.essy.Helper

sealed class ResultData {
    data class Success<out T>(val data: T) : ResultData()
    data class Error(val exception: Throwable) : ResultData()
    data class Loading(val isLoading: Boolean) : ResultData()
}