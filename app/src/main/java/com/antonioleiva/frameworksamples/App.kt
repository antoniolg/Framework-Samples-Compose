package com.antonioleiva.frameworksamples

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        val name = getString(R.string.channel_basic_name)
        val descriptionText = getString(R.string.channel_basic_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_BASIC_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_BASIC_ID = "basic_notification_channel"
    }
} 