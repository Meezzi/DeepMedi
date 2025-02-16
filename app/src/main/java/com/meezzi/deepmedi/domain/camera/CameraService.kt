package com.meezzi.deepmedi.domain.camera

import java.io.File

interface CameraService {

    fun takePicture(
        cameraController: androidx.camera.view.CameraController,
        onSuccess: (File) -> Unit,
        onError: (Exception) -> Unit
    )
}