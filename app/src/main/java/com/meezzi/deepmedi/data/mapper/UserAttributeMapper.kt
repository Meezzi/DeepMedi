package com.meezzi.deepmedi.data.mapper

import com.meezzi.deepmedi.data.model.UserAttribute
import com.meezzi.deepmedi.presentation.ui.state.HealthStatus
import com.meezzi.deepmedi.presentation.ui.state.UserInfo
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import java.util.Calendar

object UserAttributeMapper {

    fun mapToUserInfo(userAttributes: List<UserAttribute>): UserInfo {
        val gender = getGender(userAttributes)
        val age = getAge(userAttributes)
        val heartRate = getHeartRate(userAttributes)
        val (sys, dia) = getBloodPressure(userAttributes)

        val heartRateStatus = getHeartRateStatus(heartRate)
        val bloodPressureStatus = getBloodPressureStatus(sys, dia)

        return UserInfo(
            gender = gender,
            age = age,
            healthStatus = HealthStatus(
                heartRate = heartRate,
                heartRateStatus = heartRateStatus.label,
                bloodPressure = Pair(sys, dia),
                bloodPressureStatus = bloodPressureStatus.label,
                mainMessage = getMainMessage(heartRateStatus, bloodPressureStatus)
            )
        )
    }

    private fun getGender(userAttributes: List<UserAttribute>): String {
        return userAttributes.find { it.key == "gender" }?.value?.jsonPrimitive?.content?.let {
            when (it) {
                "0" -> "남성"
                "1" -> "여성"
                else -> "알 수 없음"
            }
        } ?: "알 수 없음"
    }

    private fun getAge(userAttributes: List<UserAttribute>): Int {
        val birthValue = userAttributes.find { it.key == "birth" }?.value?.jsonPrimitive?.content.orEmpty()
        if (birthValue.length < 8) return 0

        val birthYear = birthValue.substring(0, 4).toInt()
        val birthMonth = birthValue.substring(4, 6).toInt()
        val birthDay = birthValue.substring(6, 8).toInt()

        val currentDate = Calendar.getInstance()
        val birthDate = Calendar.getInstance().apply { set(birthYear, birthMonth - 1, birthDay) }

        var age = currentDate.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)
        if (currentDate.get(Calendar.MONTH) < birthDate.get(Calendar.MONTH) ||
            (currentDate.get(Calendar.MONTH) == birthDate.get(Calendar.MONTH) &&
                    currentDate.get(Calendar.DAY_OF_MONTH) < birthDate.get(Calendar.DAY_OF_MONTH))
        ) {
            age--
        }
        return age
    }

    private fun getHeartRate(userAttributes: List<UserAttribute>): Int {
        return userAttributes.find { it.key == "hr" }?.value?.jsonPrimitive?.int ?: 0
    }

    private fun getBloodPressure(userAttributes: List<UserAttribute>): Pair<Int, Int> {
        val bpString = userAttributes.find { it.key == "bp" }?.value?.jsonPrimitive?.content.orEmpty()
        val bpValues = bpString.split(",").mapNotNull { it.trim().toIntOrNull() }
        return if (bpValues.size == 2) Pair(bpValues[0], bpValues[1]) else Pair(0, 0)
    }

    private fun getHeartRateStatus(heartRate: Int): HealthStatusType {
        return when (heartRate) {
            in 0..40 -> HealthStatusType.Danger
            in 41..60 -> HealthStatusType.Warning
            in 61..80 -> HealthStatusType.Attention
            in 81..100 -> HealthStatusType.Normal
            in 101..120 -> HealthStatusType.Healthy
            else -> HealthStatusType.Danger
        }
    }

    private fun getBloodPressureStatus(sys: Int, dia: Int): HealthStatusType {
        return when {
            sys in 0..30 && dia in 0..59 -> HealthStatusType.Danger
            sys in 31..60 && dia in 60..70 -> HealthStatusType.Warning
            sys in 61..80 && dia in 71..80 -> HealthStatusType.Attention
            sys in 81..90 && dia in 81..90 -> HealthStatusType.Normal
            sys > 90 && dia > 100 -> HealthStatusType.Healthy
            else -> HealthStatusType.Danger
        }
    }

    private fun getMainMessage(heartRateStatus: HealthStatusType, bloodPressureStatus: HealthStatusType): String {
        return when {
            heartRateStatus == HealthStatusType.Normal && bloodPressureStatus == HealthStatusType.Normal -> "건강한 상태입니다."
            heartRateStatus == HealthStatusType.Danger || bloodPressureStatus == HealthStatusType.Danger -> "건강에 위험이 있습니다."
            else -> "건강에 대한 관심이 필요합니다."
        }
    }
}

enum class HealthStatusType(val label: String) {
    Normal("정상"),
    Warning("경고"),
    Danger("위험"),
    Attention("관심"),
    Healthy("건강")
}