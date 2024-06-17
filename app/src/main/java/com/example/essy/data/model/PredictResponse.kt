package com.example.essy.data.model

import com.google.gson.annotations.SerializedName

data class PredictResponse(
    @SerializedName("jawaban")
    val jawaban: String,
    @SerializedName("totalNilaiJawaban")
    val totalNilaiJawaban: Int
)