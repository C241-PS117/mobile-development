package com.example.essy.data.model

data class LoginResponse(
    val error: Boolean,
    val data: LoginResult,
    val message: String,
    val status: String
)

data class LoginResult(
    val email: String,
    val id: Int,
    val jeniskelamin: String,
    val password: String,
    val urlgambar: String,
    val username: String
)