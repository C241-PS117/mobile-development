package com.example.essy.data.network


import com.example.essy.data.model.LoginResponse;
import com.example.essy.data.model.RegisterRequest
import com.example.essy.data.model.RegisterResponse;
import retrofit2.http.Body

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @POST("api/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @FormUrlEncoded
    @POST("api/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse
}
