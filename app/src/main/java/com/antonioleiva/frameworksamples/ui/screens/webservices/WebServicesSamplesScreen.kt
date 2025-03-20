package com.antonioleiva.frameworksamples.ui.screens.webservices

import androidx.compose.runtime.Composable
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.model.Sample
import com.antonioleiva.frameworksamples.model.SamplesInfo
import com.antonioleiva.frameworksamples.ui.components.SamplesScreen
import kotlinx.serialization.Serializable

val webServicesSamples = listOf(
    Sample(
        R.string.web_services_basic_retrofit,
        R.string.web_services_basic_retrofit_description,
        BasicRetrofitScreen
    )
)

@Serializable
object WebServicesSamplesScreen

@Composable
fun WebServicesSamplesScreen(onSampleClick: (Sample) -> Unit, onBack: () -> Unit) {
    SamplesScreen(
        samplesInfo = SamplesInfo(
            R.string.topic_web_services,
            webServicesSamples
        ),
        onSampleClick = onSampleClick,
        onBack = onBack
    )
} 