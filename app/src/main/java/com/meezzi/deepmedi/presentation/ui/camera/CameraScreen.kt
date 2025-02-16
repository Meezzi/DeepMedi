package com.meezzi.deepmedi.presentation.ui.camera

import android.content.Context
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.meezzi.deepmedi.R

@Composable
fun CameraScreen(
    onNavigateToResult: () -> Unit,
    uploadViewModel: UploadViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val cameraController = rememberCameraController(context, lifecycleOwner)

    val isLoading by uploadViewModel.isLoading.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        CameraPreview(cameraController)

        if (isLoading) {
            LoadingIndicator()
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(80.dp)
        ) {
            CaptureButton(
                isEnabled = !isLoading,
                onclick = {
                    uploadViewModel.captureImage(
                        cameraController = cameraController,
                        onSuccess = { onNavigateToResult() },
                        onError = {
                            Toast.makeText(
                                context,
                                context.getString(R.string.camera_error_message),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                },
            )
        }
    }
}

@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.Blue)
    }
}

@Composable
private fun CaptureButton(
    isEnabled: Boolean,
    onclick: () -> Unit,
) {
    IconButton(
        onClick = { onclick() },
        modifier = Modifier
            .size(70.dp),
        enabled = isEnabled,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_camera_button),
            contentDescription = stringResource(id = R.string.camera),
            modifier = Modifier.size(200.dp),
            tint = Color.Unspecified
        )
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