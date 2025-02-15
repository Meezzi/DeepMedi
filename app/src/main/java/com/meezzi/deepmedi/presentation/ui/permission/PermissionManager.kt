package com.meezzi.deepmedi.presentation.ui.permission

import android.Manifest
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
class PermissionManager() {
    private val cameraPermission = Manifest.permission.CAMERA

    // 권한 관리
    @Composable
    fun rememberCameraPermissionState(): PermissionState {
        return rememberPermissionState(permission = cameraPermission)
    }

    // 권한 요청
    fun requestPermission(permissionState: PermissionState, onShowRationale: () -> Unit) {
        if (permissionState.status.shouldShowRationale) {
            // 사용자가 권한을 거부했을 경우, 권한이 필요한 이유를 설명 후, 권한 재요청
            onShowRationale()
        } else {
            // 사용자가 권한을 처음 요청한 경우, 권한 요청
            permissionState.launchPermissionRequest()
        }
    }
}