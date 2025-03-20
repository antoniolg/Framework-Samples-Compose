package com.antonioleiva.frameworksamples.ui.screens.workmanager

import androidx.compose.runtime.Composable
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.model.Sample
import com.antonioleiva.frameworksamples.model.SamplesInfo
import com.antonioleiva.frameworksamples.ui.components.SamplesScreen
import kotlinx.serialization.Serializable

val workManagerSamples = listOf(
    Sample(
        R.string.work_manager_basic_sync,
        R.string.work_manager_basic_sync_description,
        BasicSyncScreen
    ),
    Sample(
        R.string.work_manager_image_processing,
        R.string.work_manager_image_processing_description,
        ImageProcessingScreen
    ),
    Sample(
        R.string.work_manager_periodic,
        R.string.work_manager_periodic_description,
        PeriodicWorkScreen
    ),
    Sample(
        R.string.work_manager_chained,
        R.string.work_manager_chained_description,
        ChainedWorkScreen
    ),
    Sample(
        R.string.work_manager_advanced_constraints,
        R.string.work_manager_advanced_constraints_description,
        AdvancedConstraintsWorkScreen
    )
)

@Serializable
object WorkManagerSamplesScreen

@Composable
fun WorkManagerSamplesScreen(onSampleClick: (Sample) -> Unit, onBack: () -> Unit) {
    SamplesScreen(
        samplesInfo = SamplesInfo(
            R.string.topic_work_manager,
            workManagerSamples
        ),
        onSampleClick = onSampleClick,
        onBack = onBack
    )
}
