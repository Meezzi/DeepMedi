package com.meezzi.deepmedi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.meezzi.deepmedi.presentation.navigation.AppNavGraph
import com.meezzi.deepmedi.ui.theme.DeepMediTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DeepMediTheme {
                val navController = rememberNavController()
                AppNavGraph(navController)
            }
        }
    }
}