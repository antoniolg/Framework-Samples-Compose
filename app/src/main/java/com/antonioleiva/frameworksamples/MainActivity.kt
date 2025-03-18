package com.antonioleiva.frameworksamples

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.antonioleiva.frameworksamples.model.Topic
import com.antonioleiva.frameworksamples.ui.screens.HomeScreen
import com.antonioleiva.frameworksamples.ui.theme.FrameworkSamplesTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FrameworkSamplesTheme {
                HomeScreen(
                    topics = Topic.entries,
                    onTopicClick = { topic ->
                        Toast.makeText(this, "Clicked: ${getString(topic.stringRes)}", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}