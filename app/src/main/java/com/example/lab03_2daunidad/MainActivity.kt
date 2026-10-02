package com.example.lab03_2daunidad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.lab03_2daunidad.presentation.home.HomeScreen
import com.example.lab03_2daunidad.ui.theme.LAB032DAUNIDADTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            LAB032DAUNIDADTheme {
                HomeScreen()
            }
        }
    }
}