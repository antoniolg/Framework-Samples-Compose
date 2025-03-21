package com.antonioleiva.frameworksamples.ui.screens.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.ui.components.Screen
import kotlinx.serialization.Serializable

@Serializable
object CustomTypographyShapesScreen

@Composable
fun CustomTypographyShapesScreen(onBack: () -> Unit = {}) {
    Screen(
        title = stringResource(R.string.custom_typography_shapes_title),
        onBack = onBack
    ) { modifier ->
        // We'll implement this in the next example
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Coming in the next example...")
        }
    }
} 