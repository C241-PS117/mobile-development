package com.example.essy.data.model

data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String,
    val jeniskelamin: String,
    val DataGambar: String // Ini adalah string untuk URL gambar yang akan diupload
)
