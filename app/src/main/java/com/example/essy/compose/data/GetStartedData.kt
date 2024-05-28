package com.example.essy.compose.data

import androidx.annotation.RawRes
import com.example.essy.R

class GetStartedData(@RawRes val resId: Int, val title: String, val desc: String)

val listData = listOf(
    GetStartedData(
        R.raw.animation1,
        "Selamat Datang di ESSY",
        "Mulailah perjalanan Anda menuju penilaian yang lebih cerdas dengan ESSY! Temukan cara mudah dan efisien untuk menilai esai dan ujian siswa menggunakan kecerdasan buatan.",
    ),
    GetStartedData(
        R.raw.animation2,
        "Fitur Unggulan ESSY",
        "Temukan kekuatan AI di ujung jari Anda! Dari pemindaian jawaban hingga analisis otomatis, ESSY memberikan alat yang Anda butuhkan untuk mengevaluasi tugas siswa dengan cepat dan akurat.",
    ),
    GetStartedData(
        R.raw.animation3,
        "Mulai Mengeksplorasi ESSY",
        "Ayo mulai! Ikuti langkah-langkah sederhana untuk memulai menggunakan ESSY. Dengan panduan singkat ini, Anda akan siap untuk memulai menilai tugas siswa dengan mudah dan efisien.",
    ),
    GetStartedData(
        R.raw.animation4,
        "Mari Memulai Penilaian Pintar",
        "Inilah saatnya untuk menguji kekuatan ESSY! Dengan antarmuka yang intuitif dan fitur-fitur canggih, Anda akan dapat mengevaluasi jawaban siswa dengan cepat dan mendapatkan wawasan berharga tentang kemajuan mereka. Ayo mulai menilai dengan cerdas bersama ESSY!",
    ),
)