package com.antonioleiva.frameworksamples.ui.screens.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class DownloadService : Service() {
    private var isDownloading = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val url = intent?.getStringExtra("url") ?: return START_NOT_STICKY
        Log.d(TAG, "onStartCommand: $url")

        isDownloading = true
        simulateDownload(url)

        return START_NOT_STICKY
    }

    private fun simulateDownload(url: String) {
        Thread {
            // Simulamos progreso de descarga
            for (progress in 0..100 step 10) {
                if (!isDownloading) break
                Log.d(TAG, "Downloading \"$url\": $progress%")
                Thread.sleep(100)
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
}