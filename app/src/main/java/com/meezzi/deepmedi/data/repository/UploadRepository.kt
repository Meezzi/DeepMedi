package com.meezzi.deepmedi.data.repository

import com.meezzi.deepmedi.data.api.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Named

class UploadRepository @Inject constructor(
    @Named("UploadApiService") private val uploadApiService: ApiService,
    @Named("LoginApiService") private val loginApiService: ApiService
) {

    // Multipart 데이터 생성
    private fun createMultipartBody(imageFile: File): MultipartBody.Part {
        val requestFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", imageFile.name, requestFile)
    }
}