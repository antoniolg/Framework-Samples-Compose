package com.antonioleiva.frameworksamples

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.antonioleiva.frameworksamples.ui.components.Screen
import com.antonioleiva.frameworksamples.ui.screens.HomeScreen
import com.antonioleiva.frameworksamples.ui.theme.FrameworkSamplesTheme

class MainActivity : ComponentActivity() {

    private val topics = listOf(
        "2.1 Broadcast receivers",
        "2.2 Servicios",
        "2.3 Corrutinas",
        "2.4 Notificaciones",
        "2.5 Persistencia",
        "2.6 Work Manager",
        "2.7 Web services",
        "2.8 Fragments",
        "2.9 Location & Maps",
        "2.10 Estilos y temas"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FrameworkSamplesTheme {
                HomeScreen(
                    topics = topics,
                    onTopicClick = { topic ->
                        Toast.makeText(this, "Clicked: $topic", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}