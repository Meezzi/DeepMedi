package com.meezzi.deepmedi.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponse(
    @SerialName("id") val userIdentifier: UserIdentifier
)

@Serializable
data class UserIdentifier(
    @SerialName("entityType") val entityType: String,
    @SerialName("id") val id: String
)