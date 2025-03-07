package com.meezzi.deepmedi.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.meezzi.deepmedi.presentation.ui.camera.CameraScreen
import com.meezzi.deepmedi.presentation.ui.camera.UploadViewModel
import com.meezzi.deepmedi.presentation.ui.permission.PermissionScreen
import com.meezzi.deepmedi.presentation.ui.result.ResultScreen

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {

    val uploadViewModel: UploadViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Permission.route
    ) {
        composable(Screen.Permission.route) {
            PermissionScreen(
                onNavigateToCamera = {
                    navController.navigate(Screen.Camera.route) {
                        popUpTo(Screen.Permission.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.Camera.route) {
            CameraScreen(
                onNavigateToResult = {
                    navController.navigate(Screen.Result.route)
                },
                uploadViewModel = uploadViewModel,
            )
        }

        composable(Screen.Result.route) {
            ResultScreen(
                uploadViewModel = uploadViewModel,
            )
        }
    }
}