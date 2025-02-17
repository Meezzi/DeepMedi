package com.meezzi.deepmedi.presentation.ui.camera

import androidx.camera.view.CameraController
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meezzi.deepmedi.data.exception.UploadRepositoryException
import com.meezzi.deepmedi.data.model.UserAttribute
import com.meezzi.deepmedi.data.repository.UploadRepository
import com.meezzi.deepmedi.domain.camera.CameraService
import com.meezzi.deepmedi.presentation.ui.state.HealthStatus
import com.meezzi.deepmedi.presentation.ui.state.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import java.io.File
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val cameraService: CameraService,
    private val uploadRepository: UploadRepository,
) : ViewModel() {

    // UserInfo 하나로 묶어서 관리
    private val _userInfo = MutableStateFlow(
        UserInfo(
            gender = "여성",
            age = 31,
            healthStatus = HealthStatus(
                heartRate = 88,
                heartRateStatus = "정상",
                bloodPressure = Pair(133, 74),
                bloodPressureStatus = "위험",
                mainMessage = "건강에 대한 관심이 필요합니다."
            )
        )
    )
    val userInfo: StateFlow<UserInfo> = _userInfo

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    fun captureImage(
        cameraController: CameraController,
        onSuccess: (File) -> Unit,
        onError: (Exception) -> Unit,
    ) {
        _isLoading.value = true

        cameraService.takePicture(
            cameraController = cameraController,
            onSuccess = {
                _isLoading.value = false
                uploadImage(it)
                onSuccess(it)
            },
            onError = {
                _isLoading.value = false
                onError(it)
            }
        )
    }

    private fun uploadImage(imageFile: File) {
        viewModelScope.launch {
            try {
                _userAttributes.value =
                    uploadRepository.performImageUploadAndFetchUserAttributes(imageFile)
            } catch (e: UploadRepositoryException) {
                _errorMessage.value = e.message
            } catch (e: Exception) {
                _errorMessage.value = "예상치 못한 오류가 발생했습니다."
            }
        }
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

    private fun extractAge(userAttributes: List<UserAttribute>): Int {
        val birthValue =
            userAttributes.find { it.key == "birth" }?.value?.jsonPrimitive?.content ?: ""
        if (birthValue.isNotEmpty()) {
            val birthYear = birthValue.substring(0, 4).toInt()
            val birthMonth = birthValue.substring(4, 6).toInt()
            val birthDay = birthValue.substring(6, 8).toInt()

            val currentDate = Calendar.getInstance()
            val birthDate = Calendar.getInstance().apply {
                set(birthYear, birthMonth - 1, birthDay)
            }
            var age = currentDate.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)
            if (currentDate.get(Calendar.MONTH) < birthDate.get(Calendar.MONTH) ||
                (currentDate.get(Calendar.MONTH) == birthDate.get(Calendar.MONTH) &&
                        currentDate.get(Calendar.DAY_OF_MONTH) < birthDate.get(Calendar.DAY_OF_MONTH))
            ) {
                age--
            }
            return age
        }
        return 0
    }

    private fun extractHeartRateStatus(userAttributes: List<UserAttribute>): String {
        val heartRateValue =
            userAttributes.find { it.key == "hr" }?.value?.jsonPrimitive?.int ?: return "정상"

        return when (heartRateValue) {
            in 0..40 -> "위험"
            in 41..60 -> "경고"
            in 61..80 -> "관심"
            in 81..100 -> "정상"
            in 101..120 -> "건강"
            else -> "위험"
        }
    }
}