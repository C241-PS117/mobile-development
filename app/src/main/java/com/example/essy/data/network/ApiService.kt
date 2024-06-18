package com.example.essy.data.network

import com.example.essy.data.model.EditProfileResponse
import com.example.essy.data.model.LoginResponse
import com.example.essy.data.model.PredictResponse
import com.example.essy.data.model.QuestionResponse
import com.example.essy.data.model.RegisterRequest
import com.example.essy.data.model.RegisterResponse
import com.example.essy.data.model.TambahNilaiResponse
import com.example.essy.data.model.TambahSoalResponse
import com.example.essy.data.model.UpdatePasswordResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
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
        @Part("idGuru") idGuru: RequestBody,
        @Part("soal") soal: RequestBody,
        @Part("jawaban") jawaban: RequestBody
    ): TambahSoalResponse

    @Multipart
    @POST("api/editprofil")
    suspend fun editprofil(
        @Part("id") id: RequestBody,
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part("jeniskelamin") jeniskelamin: RequestBody,
        @Part("DataGambar") DataGambar: RequestBody?
    ): EditProfileResponse

    @Multipart
    @POST("api/soal")
    suspend fun getSoal(
        @Part("idGuru") idGuru: RequestBody
    ): QuestionResponse

    @Multipart
    @POST("api/ubahpassword")
    suspend fun ubahPassword(
        @Part("id") id: RequestBody,
        @Part("oldPassword") oldPassword: RequestBody,
        @Part("newPassword") newPassword: RequestBody
    ): UpdatePasswordResponse

    @Multipart
    @POST("predict")
    suspend fun predict(
        @Part image: MultipartBody.Part,
        @Part("jawaban") jawaban: RequestBody
    ): PredictResponse

    @Multipart
    @POST("api/tambahnilai")
    suspend fun tambahNilai(
        @Part("idSoal") idSoal: RequestBody,
        @Part("idGuru") idGuru: RequestBody,
        @Part("namaSiswa") namaSiswa: RequestBody,
        @Part("nilaiSiswa") nilaiSiswa: RequestBody
    ): TambahNilaiResponse
}
