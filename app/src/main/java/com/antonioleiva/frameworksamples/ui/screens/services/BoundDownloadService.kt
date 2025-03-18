package com.antonioleiva.frameworksamples.ui.screens.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class BoundDownloadService : Service() {
    private var isDownloading = false
    private var progress = 0
    private val binder = DownloadBinder()
    private var progressListener: ((Int, String) -> Unit)? = null
    private var currentUrl = ""

    inner class DownloadBinder : Binder() {
        fun getService(): BoundDownloadService = this@BoundDownloadService
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "onBind")
        return binder
    }

    fun startDownload(url: String) {
        if (isDownloading) return
        
        Log.d(TAG, "startDownload: $url")
        isDownloading = true
        progress = 0
        currentUrl = url

        notifyProgressUpdate()
        simulateDownload()
    }

    fun stopDownload() {
        if (!isDownloading) return
        
        isDownloading = false
        Log.d(TAG, "stopDownload")
    }

    private fun simulateDownload() {
        Thread {
            progress = 0
            while (progress <= 100 && isDownloading) {
                Log.d(TAG, "Downloading \"$currentUrl\": $progress%")
                notifyProgressUpdate()
                
                if (progress == 100) {
                    break
                }
                
                Thread.sleep(200)
                progress += 10
            }
            
            if (isDownloading) {
                isDownloading = false
            }
        }.start()
    }

    private fun notifyProgressUpdate() {
        progressListener?.invoke(progress, currentUrl)
    }

    fun setProgressListener(listener: (Int, String) -> Unit) {
        progressListener = listener
    }

    fun removeProgressListener() {
        progressListener = null
    }

    override fun onDestroy() {
        super.onDestroy()
        isDownloading = false
        Log.d(TAG, "onDestroy")
    }

    companion object {
        private const val TAG = "BoundDownloadService"
    }
} 