package com.example.essy.data.model

data class QuestionResponse(
    val status: String,
    val message: String,
    val data: List<QuestionResult>
)
data class QuestionResult (
    val id: Int,
    val soal: String,
    val jawaban: String,
    val totaljawaban: Int
)