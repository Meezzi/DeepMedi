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
            gender = "알 수 없음",
            age = 0,
            healthStatus = HealthStatus(
                heartRate = 0,
                heartRateStatus = "알 수 없음",
                bloodPressure = Pair(0, 0),
                bloodPressureStatus = "알 수 없음",
                mainMessage = "알 수 없음",
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
                _isLoading.value = true
                val userAttributes =
                    uploadRepository.performImageUploadAndFetchUserAttributes(imageFile)
                updateUserInfo(userAttributes)
                _isLoading.value = false
            } catch (e: UploadRepositoryException) {
                _isLoading.value = false
                _errorMessage.value = e.message.toString()
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "예상치 못한 오류가 발생했습니다."
            }
        }
    }
}