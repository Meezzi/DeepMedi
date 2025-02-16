package com.meezzi.deepmedi.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageUploadResponse(
    val code: Int,
    val message: UploadMessage,
)

@Serializable
data class UploadMessage(
    @SerialName("message") val message: String,
    @SerialName("email") val email: String,
    @SerialName("pw") val pw: String,
)