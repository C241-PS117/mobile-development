package com.example.essy.data.model

import com.google.gson.annotations.SerializedName

data class PredictResponse(
    @SerializedName("Hasil Ocr")
    val hasilOcr: String,

    @SerializedName("Kunci Jawaban")
    val kunciJawaban: String,

    @SerializedName("Total Nilai Jawaban")
    val totalNilaiJawaban: Int
)
