package com.meezzi.deepmedi.data.api

import com.meezzi.deepmedi.data.model.AuthRequest
import com.meezzi.deepmedi.data.model.AuthResponse
import com.meezzi.deepmedi.data.model.ImageUploadResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @Multipart
    @POST("deepmedi-test-first")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): Response<ImageUploadResponse>

    @POST("api/auth/login")
    suspend fun authenticateUser(
        @Body loginRequest: AuthRequest
    ): Response<AuthResponse>
}