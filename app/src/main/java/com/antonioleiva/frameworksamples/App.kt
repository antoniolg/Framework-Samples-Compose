package com.antonioleiva.frameworksamples

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.room.Room
import com.antonioleiva.frameworksamples.persistence.TodoDatabase

class App : Application() {

    lateinit var database: TodoDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
        initDatabase()
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

    private fun initDatabase() {
        database = Room.databaseBuilder(
            this,
            TodoDatabase::class.java,
            "todo_database"
        ).build()
    }

    companion object {
        const val CHANNEL_BASIC_ID = "basic_notification_channel"
    }
} 