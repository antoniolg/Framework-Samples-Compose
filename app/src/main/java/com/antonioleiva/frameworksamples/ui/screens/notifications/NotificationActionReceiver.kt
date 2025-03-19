package com.antonioleiva.frameworksamples.ui.screens.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat

class NotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_MARK_AS_READ -> {
                NotificationManagerCompat.from(context).cancel(NOTIFICATION_WITH_ACTIONS_ID)
            }
        }
    }

    companion object {
        const val ACTION_MARK_AS_READ = "com.antonioleiva.frameworksamples.ACTION_MARK_AS_READ"
        const val NOTIFICATION_WITH_ACTIONS_ID = 2
        const val NOTIFICATION_WITH_PROGRESS_ID = 3
    }
} 