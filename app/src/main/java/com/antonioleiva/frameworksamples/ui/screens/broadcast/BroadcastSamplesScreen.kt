package com.antonioleiva.frameworksamples.ui.screens.broadcast

import androidx.compose.runtime.Composable
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.model.Sample
import com.antonioleiva.frameworksamples.model.SamplesInfo
import com.antonioleiva.frameworksamples.ui.components.SamplesScreen
import kotlinx.serialization.Serializable

val broadcastSamples = listOf(
    Sample(
        R.string.battery_sample_title,
        R.string.battery_sample_description,
        TODO()
    ),
    Sample(
        R.string.custom_broadcast_sample_title,
        R.string.custom_broadcast_sample_description,
        TODO()
    )
)

@Serializable
object BroadcastSamplesScreen

@Composable
fun BroadcastSamplesScreen(onSampleClick: (Sample) -> Unit, onBack: () -> Unit) {
    SamplesScreen(
        samplesInfo = SamplesInfo(
            R.string.topic_broadcast_receivers,
            broadcastSamples
        ),
        onSampleClick = onSampleClick,
        onBack = onBack
    )
}