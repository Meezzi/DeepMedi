package com.meezzi.deepmedi.presentation.ui.camera

import androidx.camera.view.CameraController
import androidx.lifecycle.ViewModel
import com.meezzi.deepmedi.domain.camera.CameraService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val cameraService: CameraService,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

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
                onSuccess(it)
            },
            onError = {
                _isLoading.value = false
                onError(it)
            }
        )
    }
}