package com.antonioleiva.frameworksamples.ui.screens.theme

import androidx.compose.runtime.Composable
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.model.Sample
import com.antonioleiva.frameworksamples.model.SamplesInfo
import com.antonioleiva.frameworksamples.ui.components.SamplesScreen
import kotlinx.serialization.Serializable

val themeSamples = listOf(
    Sample(
        R.string.theme_selector_title,
        R.string.theme_selector_description,
        ThemeSelectorScreen
    ),
    Sample(
        R.string.custom_typography_shapes_title,
        R.string.custom_typography_shapes_description,
        CustomTypographyShapesScreen
    )
)

@Serializable
object ThemeSamplesScreen

@Composable
fun ThemeSamplesScreen(onSampleClick: (Sample) -> Unit, onBack: () -> Unit) {
    SamplesScreen(
        samplesInfo = SamplesInfo(
            R.string.topic_styles_themes,
            themeSamples
        ),
        onSampleClick = onSampleClick,
        onBack = onBack
    )
} 