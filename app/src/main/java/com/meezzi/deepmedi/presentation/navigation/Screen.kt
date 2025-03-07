package com.meezzi.deepmedi.presentation.navigation

sealed class Screen(val route: String) {
    data object Permission: Screen("permission")
    data object Camera: Screen("camera")
    data object Result: Screen("result")
}