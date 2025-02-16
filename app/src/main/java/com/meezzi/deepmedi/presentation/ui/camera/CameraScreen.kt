package com.meezzi.deepmedi.presentation.ui.camera

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner

@Composable
fun CameraScreen(
    onNavigateToResult: () -> Unit,
    cameraViewModel: CameraViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val cameraController = rememberCameraController(context, lifecycleOwner)

    Box(modifier = Modifier.fillMaxSize()) {
        CameraPreview(cameraController)
    }
}

// 카메라 미리보기 화면
@Composable
private fun CameraPreview(cameraController: LifecycleCameraController) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            // PreviewView 초기화 및 설정
            PreviewView(ctx).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
                implementationMode = PreviewView.ImplementationMode.PERFORMANCE
                controller = cameraController
            }
        },
        onRelease = {
            // 카메라 화면에서 사라지면 카메라 리소스 해제
            cameraController.unbind()
        }
    )
}

@Composable
fun rememberCameraController(
    context: Context,
    lifecycleOwner: LifecycleOwner,
): LifecycleCameraController {
    return remember {
        LifecycleCameraController(context).apply {
            cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build()
            bindToLifecycle(lifecycleOwner)
        }
    }
}