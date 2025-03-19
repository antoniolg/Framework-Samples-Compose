package com.antonioleiva.frameworksamples.ui.screens.services

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.antonioleiva.frameworksamples.App
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.ui.screens.notifications.NotificationActionReceiver

class DownloadService : Service() {
    private var isDownloading = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val url = intent?.getStringExtra("url") ?: return START_NOT_STICKY
        Log.d(TAG, "onStartCommand: $url")

        isDownloading = true
        startForeground(
            NotificationActionReceiver.NOTIFICATION_WITH_PROGRESS_ID,
            createNotification(0)
        )
        simulateDownload(url)

        return START_NOT_STICKY
    }

    private fun simulateDownload(url: String) {
        Thread {
            // Simulamos progreso de descarga
            for (progress in 0..100 step 10) {
                if (!isDownloading) break
                Log.d("DownloadService", "Downloading \"$url\": $progress%")
                updateNotification(progress)
                Thread.sleep(500)
            }

            if (isDownloading) {
                updateNotification(100, true)
            }

            stopSelf()
        }.start()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        isDownloading = false
        Log.d(TAG, "onDestroy")
    }

    companion object {
        private const val TAG = "DownloadService"
    }

    private fun createNotification(progress: Int, isComplete: Boolean = false) =
        NotificationCompat.Builder(this, App.CHANNEL_BASIC_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    if (isComplete) R.string.notification_download_complete
                    else R.string.notification_download_title
                )
            )
            .setContentText(getString(R.string.notification_download_content, progress))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(!isComplete)
            .setProgress(100, progress, false)
            .build()

    private fun updateNotification(progress: Int, isComplete: Boolean = false) {
        if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PERMISSION_GRANTED) {
            NotificationManagerCompat.from(this)
                .notify(
                    NotificationActionReceiver.NOTIFICATION_WITH_PROGRESS_ID,
                    createNotification(progress, isComplete)
                )
        }
    }
}