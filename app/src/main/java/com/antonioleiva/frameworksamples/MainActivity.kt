package com.antonioleiva.frameworksamples

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.antonioleiva.frameworksamples.ui.navigation.Navigation
import com.antonioleiva.frameworksamples.ui.theme.FrameworkSamplesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FrameworkSamplesTheme {
                Navigation()
            }
        }
    }
}