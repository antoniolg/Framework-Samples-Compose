package com.antonioleiva.frameworksamples.ui.screens.persistence

import androidx.compose.runtime.Composable
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.model.Sample
import com.antonioleiva.frameworksamples.model.SamplesInfo
import com.antonioleiva.frameworksamples.ui.components.SamplesScreen
import kotlinx.serialization.Serializable

val persistenceSamples = listOf(
    Sample(
        titleRes = R.string.shared_preferences,
        descriptionRes = R.string.shared_preferences_description,
        destination = SharedPreferencesScreen
    )
)

@Serializable
object PersistenceSamplesScreen

@Composable
fun PersistenceSamplesScreen(
    onSampleClick: (Sample) -> Unit,
    onBack: () -> Unit
) {
    SamplesScreen(
        samplesInfo = SamplesInfo(
            title = R.string.topic_persistence,
            samples = persistenceSamples
        ),
        onSampleClick = onSampleClick,
        onBack = onBack
    )
} 