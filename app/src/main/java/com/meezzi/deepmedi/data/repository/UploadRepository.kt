package com.meezzi.deepmedi.data.repository

import com.meezzi.deepmedi.data.api.ApiService
import com.meezzi.deepmedi.data.exception.UploadRepositoryException
import com.meezzi.deepmedi.data.model.AuthRequest
import com.meezzi.deepmedi.data.model.ImageUploadResponse
import com.meezzi.deepmedi.data.model.UserAttribute
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

    // 1. 이미지 업로드
    private suspend fun uploadImage(imageFile: File): ImageUploadResponse {
        val imagePart = createMultipartBody(imageFile)
        val response = uploadApiService.uploadImage(imagePart)

        return if (response.isSuccessful) {
            response.body() ?: throw UploadRepositoryException("업로드 응답이 없습니다.")
        } else {
            throw UploadRepositoryException("업로드 실패: ${response.errorBody()?.string()}")
        }
    }

    // 2. 사용자 인증 요청
    private suspend fun authenticateUser(email: String, password: String): String {
        val authRequest = AuthRequest(username = email, password = password)
        val response = loginApiService.authenticateUser(authRequest)

        return if (response.isSuccessful) {
            response.body()?.token ?: throw UploadRepositoryException("사용자 인증 응답이 없습니다.")
        } else {
            throw UploadRepositoryException("사용자 인증 실패: ${response.errorBody()?.string()}")
        }
    }

    // 3. 사용자 정보 가져오기
    private suspend fun fetchUserIdFromToken(token: String): String {
        val response = loginApiService.fetchUserInfo("Bearer $token")

        return if (response.isSuccessful) {
            response.body()?.userIdentifier?.id
                ?: throw UploadRepositoryException("사용자 정보가 없습니다.")
        } else {
            throw UploadRepositoryException("사용자 정보 요청 실패: ${response.errorBody()?.string()}")
        }
    }

    // 4. 사용자 속성 조회
    private suspend fun fetchUserAttributes(userId: String, token: String): List<UserAttribute> {
        val response = loginApiService.fetchUserAttributes(userId, "Bearer $token")

        return if (response.isSuccessful) {
            response.body() ?: throw UploadRepositoryException("사용자 정보가 없습니다.")
        } else {
            throw UploadRepositoryException("사용자 속성 요청 실패: ${response.errorBody()?.string()}")
        }
    }

    // Multipart 데이터 생성
    private fun createMultipartBody(imageFile: File): MultipartBody.Part {
        val requestFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", imageFile.name, requestFile)
    }
}