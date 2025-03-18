package com.antonioleiva.frameworksamples.ui.screens.coroutines

import androidx.compose.runtime.Composable
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.model.Sample
import com.antonioleiva.frameworksamples.model.SamplesInfo
import com.antonioleiva.frameworksamples.ui.components.SamplesScreen
import kotlinx.serialization.Serializable

val coroutineSamples = listOf(
    Sample(
        R.string.coroutines_title,
        R.string.coroutines_description,
        DispatchersScreen
    )
)

@Serializable
object CoroutineSamplesScreen

@Composable
fun CoroutineSamplesScreen(onSampleClick: (Sample) -> Unit, onBack: () -> Unit) {
    SamplesScreen(
        samplesInfo = SamplesInfo(
            R.string.topic_coroutines,
            coroutineSamples
        ),
        onSampleClick = onSampleClick,
        onBack = onBack
    )
} 