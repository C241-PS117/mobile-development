package com.example.essy.data.model

data class NilaiResponse(
    val status: String,
    val message: String,
    val data: List<NilaiResult>
)

data class NilaiResult(
    val id: Int,
    val namaSiswa: String,
    val nilaiSiswa: Int
)
