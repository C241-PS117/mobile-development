package com.example.essy.utils

sealed class ResultData<out T> {
    data class Success<out T>(val data: T) : ResultData<T>()
    data class Error(val exception: Exception) : ResultData<Nothing>()
    object Loading : ResultData<Nothing>()
}