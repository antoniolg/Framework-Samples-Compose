package com.antonioleiva.frameworksamples.model

import com.antonioleiva.frameworksamples.R

enum class Topic(val stringRes: Int) {
    BROADCAST_RECEIVERS(R.string.topic_broadcast_receivers),
    SERVICES(R.string.topic_services),
    COROUTINES(R.string.topic_coroutines),
    NOTIFICATIONS(R.string.topic_notifications),
    PERSISTENCE(R.string.topic_persistence),
    WORK_MANAGER(R.string.topic_work_manager),
    WEB_SERVICES(R.string.topic_web_services),
    FRAGMENTS(R.string.topic_fragments),
    LOCATION_MAPS(R.string.topic_location_maps),
    STYLES_THEMES(R.string.topic_styles_themes);
} 