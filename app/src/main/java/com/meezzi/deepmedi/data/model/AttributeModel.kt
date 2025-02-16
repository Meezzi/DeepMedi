package com.meezzi.deepmedi.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class UserAttribute(
    @SerialName("lastUpdateTs") val lastUpdateTs: Long,
    @SerialName("key") val key: String,
    @SerialName("value") val value: JsonElement
)