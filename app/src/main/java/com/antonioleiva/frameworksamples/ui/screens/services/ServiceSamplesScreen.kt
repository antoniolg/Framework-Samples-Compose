package com.antonioleiva.frameworksamples.ui.screens.services

import androidx.compose.runtime.Composable
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.model.Sample
import com.antonioleiva.frameworksamples.model.SamplesInfo
import com.antonioleiva.frameworksamples.ui.components.SamplesScreen
import kotlinx.serialization.Serializable

val serviceSamples = listOf(
    Sample(
        R.string.download_service_title,
        R.string.download_service_description,
        DownloadServiceScreen
    ),
    Sample(
        R.string.bound_download_service_title,
        R.string.bound_download_service_description,
        BoundDownloadServiceScreen
    )
)

@Serializable
object ServiceSamplesScreen

@Composable
fun ServiceSamplesScreen(onSampleClick: (Sample) -> Unit, onBack: () -> Unit) {
    SamplesScreen(
        samplesInfo = SamplesInfo(
            R.string.topic_services,
            serviceSamples
        ),
        onSampleClick = onSampleClick,
        onBack = onBack
    )
} 