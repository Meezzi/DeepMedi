package com.meezzi.deepmedi.presentation.ui.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
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

    // 앱 설정 화면에서 직접 권한 변경
    fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            data = Uri.parse("package:${context.packageName}")
        }
        context.startActivity(intent)
    }
}