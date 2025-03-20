package com.antonioleiva.frameworksamples.ui.screens.fragments

import androidx.compose.runtime.Composable
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.model.Sample
import com.antonioleiva.frameworksamples.model.SamplesInfo
import com.antonioleiva.frameworksamples.ui.components.SamplesScreen
import kotlinx.serialization.Serializable

val fragmentsSamples = listOf(
    Sample(
        R.string.fragments_basic_title,
        R.string.fragments_basic_description,
        BasicFragmentSample
    ),
    Sample(
        R.string.fragments_navigation_title,
        R.string.fragments_navigation_description,
        NavigationFragmentSample
    )
)

@Serializable
object FragmentsSamplesScreen

@Serializable
object BasicFragmentSample

@Serializable
object NavigationFragmentSample

@Composable
fun FragmentsSamplesScreen(onSampleClick: (Sample) -> Unit, onBack: () -> Unit) {
    SamplesScreen(
        samplesInfo = SamplesInfo(
            R.string.topic_fragments,
            fragmentsSamples
        ),
        onSampleClick = onSampleClick,
        onBack = onBack
    )
} 