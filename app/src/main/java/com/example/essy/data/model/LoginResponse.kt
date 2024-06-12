package com.example.essy.data.model

data class LoginResponse(
    val data: Data,
    val message: String,
    val status: String
)

data class Data(
    val email: String,
    val id: Int,
    val jeniskelamin: String,
    val password: String,
    val urlgambar: String,
    val username: String
)