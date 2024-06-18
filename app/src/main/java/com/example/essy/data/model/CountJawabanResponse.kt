package com.example.essy.data.model

data class CountJawabanResponse(
    val data: List<CountResult>,
    val message: String,
    val status: String
)

data class CountResult(
    val TotalJawaban: Int,
    val TotalSoal: Int
)