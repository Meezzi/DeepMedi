package com.meezzi.deepmedi.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.meezzi.deepmedi.presentation.ui.camera.CameraScreen
import com.meezzi.deepmedi.presentation.ui.permission.PermissionScreen
import com.meezzi.deepmedi.presentation.ui.result.ResultScreen

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Camera.route
    ) {
        composable(Screen.Permission.route) {
            PermissionScreen(
                onNavigateToCamera = { navController.navigate(Screen.Camera.route) }
            )
        }

        composable(Screen.Camera.route) {
            CameraScreen()
        }

        composable(Screen.Result.route) {
            ResultScreen()
        }
    }
}