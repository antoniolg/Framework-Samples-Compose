package com.antonioleiva.frameworksamples.model

import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.ui.screens.broadcast.BroadcastSamplesScreen
import com.antonioleiva.frameworksamples.ui.screens.coroutines.CoroutineSamplesScreen
import com.antonioleiva.frameworksamples.ui.screens.notifications.NotificationSamplesScreen
import com.antonioleiva.frameworksamples.ui.screens.persistence.PersistenceSamplesScreen
import com.antonioleiva.frameworksamples.ui.screens.services.ServiceSamplesScreen
import com.antonioleiva.frameworksamples.ui.screens.workmanager.WorkManagerSamplesScreen

enum class Topic(val stringRes: Int, val destination: Any) {
    BROADCAST_RECEIVERS(R.string.topic_broadcast_receivers, BroadcastSamplesScreen),
    SERVICES(R.string.topic_services, ServiceSamplesScreen),
    COROUTINES(R.string.topic_coroutines, CoroutineSamplesScreen),
    NOTIFICATIONS(R.string.topic_notifications, NotificationSamplesScreen),
    PERSISTENCE(R.string.topic_persistence, PersistenceSamplesScreen),
    WORK_MANAGER(R.string.topic_work_manager, WorkManagerSamplesScreen),
    WEB_SERVICES(R.string.topic_web_services, Unit),
    FRAGMENTS(R.string.topic_fragments, Unit),
    LOCATION_MAPS(R.string.topic_location_maps, Unit),
    STYLES_THEMES(R.string.topic_styles_themes, Unit);
} 