package com.example.tugasakhir.data.api

import com.example.tugasakhir.data.model.LoginResponse
import com.example.tugasakhir.data.model.PasienResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("api/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @GET("api/pasien")
    suspend fun getPasien(): Response<PasienResponse>
}
