package com.meezzi.deepmedi.presentation.ui.permission

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.meezzi.deepmedi.R

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionScreen(
    onNavigateToCamera: () -> Unit,
) {
    val context = LocalContext.current
    val permissionManager = PermissionManager()
    val permissionState = permissionManager.rememberCameraPermissionState()

    var showDialog by remember { mutableStateOf(false) }

    if (permissionState.status.isGranted) {
        onNavigateToCamera()
    } else {
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                if (showDialog) {
                    PermissionRationaleDialog(
                        onConfirm = { permissionManager.openAppSettings(context) },
                        onDismiss = { showDialog = false },
                    )
                }

                PermissionContent()

                Spacer(modifier = Modifier.weight(1f))

                PermissionButton(
                    onRequestPermission = {
                        permissionManager.requestPermission(permissionState) {
                            showDialog = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun PermissionContent() {
    Column(modifier = Modifier.padding(30.dp)) {
        Text(
            text = stringResource(R.string.permission_title),
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.required_permission),
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(16.dp))

        PermissionItem(
            iconPainter = painterResource(R.drawable.ic_camera),
            permissionTitle = stringResource(R.string.camera),
            permissionLabel = stringResource(R.string.permission_camera_label)
        )
    }
}

@Composable
private fun PermissionItem(
    iconPainter: Painter,
    permissionTitle: String,
    permissionLabel: String,
) {
    Row {
        Image(
            painter = iconPainter,
            contentDescription = permissionTitle,
            modifier = Modifier
                .size(36.dp),
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Column {
            Text(
                text = permissionTitle,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = permissionLabel,
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun PermissionButton(
    onRequestPermission: () -> Unit,
) {
    Button(
        onClick = onRequestPermission,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RectangleShape
    ) {
        Text(
            text = stringResource(R.string.confirm),
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun PermissionRationaleDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(text = stringResource(R.string.setting))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = stringResource(R.string.close))
            }
        },
        title = { Text(text = stringResource(R.string.permission_dialog_title)) },
        text = { Text(text = stringResource(R.string.permission_dialog_content)) },
    )
}