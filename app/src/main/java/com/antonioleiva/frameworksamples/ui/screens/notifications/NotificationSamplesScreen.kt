package com.antonioleiva.frameworksamples.ui.screens.notifications

import androidx.compose.runtime.Composable
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.model.Sample
import com.antonioleiva.frameworksamples.model.SamplesInfo
import com.antonioleiva.frameworksamples.ui.components.SamplesScreen
import com.antonioleiva.frameworksamples.ui.screens.services.DownloadServiceScreen
import kotlinx.serialization.Serializable

private val notificationSamples = listOf(
    Sample(
        titleRes = R.string.notification_basic,
        descriptionRes = R.string.notification_basic_description,
        destination = BasicNotificationScreen
    ),
    Sample(
        titleRes = R.string.notification_actions,
        descriptionRes = R.string.notification_actions_description,
        destination = ActionsNotificationScreen
    ),
    Sample(
        titleRes = R.string.notification_progress,
        descriptionRes = R.string.notification_progress_description,
        destination = DownloadServiceScreen
    )
)

@Serializable
object NotificationSamplesScreen

@Composable
fun NotificationSamplesScreen(
    onSampleClick: (Sample) -> Unit,
    onBack: () -> Unit
) {
    SamplesScreen(
        samplesInfo = SamplesInfo(
            title = R.string.topic_notifications,
            samples = notificationSamples
        ),
        onSampleClick = onSampleClick,
        onBack = onBack
    )
} 