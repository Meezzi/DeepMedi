package com.meezzi.deepmedi.presentation.ui.state

data class UserInfo(
    val gender: String,
    val age: Int,
    val healthStatus: HealthStatus,
)

data class HealthStatus(
    val heartRate: Int,
    val heartRateStatus: String,
    val bloodPressure: Pair<Int, Int>,
    val bloodPressureStatus: String,
    val mainMessage: String,
)