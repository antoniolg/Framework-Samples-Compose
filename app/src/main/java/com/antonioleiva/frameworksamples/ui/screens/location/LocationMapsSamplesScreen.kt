package com.antonioleiva.frameworksamples.ui.screens.location

import androidx.compose.runtime.Composable
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.model.Sample
import com.antonioleiva.frameworksamples.model.SamplesInfo
import com.antonioleiva.frameworksamples.ui.components.SamplesScreen
import kotlinx.serialization.Serializable

@Serializable
object LocationMapsSamplesScreen

@Serializable
object BasicLocationScreen

@Serializable
object MapScreen

@Composable
fun LocationMapsSamplesScreen(
    onSampleClick: (Sample) -> Unit,
    onBack: () -> Unit = {}
) {
    SamplesScreen(
        samplesInfo = SamplesInfo(
            title = R.string.topic_location_maps,
            samples = listOf(
                Sample(
                    titleRes = R.string.location_basic_title,
                    descriptionRes = R.string.location_basic_description,
                    destination = BasicLocationScreen
                ),
                Sample(
                    titleRes = R.string.map_title,
                    descriptionRes = R.string.map_description,
                    destination = MapScreen
                )
                // Add more location samples here
            )
        ),
        onSampleClick = onSampleClick,
        onBack = onBack
    )
} 