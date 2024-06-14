package com.example.essy.data.network


import com.example.essy.data.model.EditProfileResponse
import com.example.essy.data.model.LoginResponse;
import com.example.essy.data.model.RegisterRequest
import com.example.essy.data.model.RegisterResponse;
import com.example.essy.data.model.TambahSoalResponse
import okhttp3.RequestBody
import retrofit2.http.Body

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @POST("api/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @Multipart
    @POST("api/login")
    suspend fun login(
        @Part("username") username: RequestBody,
        @Part("password") password: RequestBody
    ): LoginResponse

    @Multipart
    @POST("api/tambahsoal")
    suspend fun tambahSoal(
        @Part("soal") soal: RequestBody,
        @Part("jawaban") jawaban: RequestBody
    ): TambahSoalResponse

    @Multipart
    @POST("api/editprofile")
    suspend fun editprofil(
        @Part("soal") soal: RequestBody,
        @Part("jawaban") jawaban: RequestBody
    ): EditProfileResponse
}
