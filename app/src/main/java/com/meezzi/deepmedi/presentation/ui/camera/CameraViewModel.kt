package com.meezzi.deepmedi.presentation.ui.camera

import androidx.camera.view.CameraController
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meezzi.deepmedi.data.model.UserAttribute
import com.meezzi.deepmedi.data.repository.UploadRepository
import com.meezzi.deepmedi.domain.camera.CameraService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val cameraService: CameraService,
    private val uploadRepository: UploadRepository,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _userAttributes = MutableStateFlow<List<UserAttribute>>(emptyList())
    val userAttributes: StateFlow<List<UserAttribute>> = _userAttributes

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
            _userAttributes.value =
                uploadRepository.performImageUploadAndFetchUserAttributes(imageFile)
        }
    }
}