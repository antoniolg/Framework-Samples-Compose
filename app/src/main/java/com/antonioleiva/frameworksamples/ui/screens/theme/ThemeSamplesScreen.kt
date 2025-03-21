package com.antonioleiva.frameworksamples.ui.screens.theme

import androidx.compose.runtime.Composable
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.model.Sample
import com.antonioleiva.frameworksamples.model.SamplesInfo
import com.antonioleiva.frameworksamples.ui.components.SamplesScreen
import kotlinx.serialization.Serializable

private val themeSamples = listOf(
    Sample(
        titleRes = R.string.theme_selector_title,
        descriptionRes = R.string.theme_selector_desc,
        destination = ThemeSelectorScreen
    ),
    Sample(
        titleRes = R.string.theme_custom_typo_shapes_title,
        descriptionRes = R.string.theme_custom_typo_shapes_desc,
        destination = CustomTypographyShapesScreen
    ),
    Sample(
        titleRes = R.string.theme_component_wrapper_title,
        descriptionRes = R.string.theme_component_wrapper_desc,
        destination = ComponentWrapperScreen
    )
)

@Serializable
object ThemeSamplesScreen

@Composable
fun ThemeSamplesScreen(
    onSampleClick: (Sample) -> Unit,
    onBack: () -> Unit
) {
    SamplesScreen(
        samplesInfo = SamplesInfo(
            title = R.string.topic_styles_themes,
            samples = themeSamples
        ),
        onSampleClick = onSampleClick,
        onBack = onBack
    )
} 