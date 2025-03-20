package com.antonioleiva.frameworksamples.ui.screens.workmanager.workers

import android.content.Context
import android.content.SharedPreferences
import androidx.core.app.NotificationCompat
import androidx.core.content.edit
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.antonioleiva.frameworksamples.App
import com.antonioleiva.frameworksamples.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Worker that simulates periodic maintenance tasks.
 * Saves information about each execution in SharedPreferences.
 */
class PeriodicMaintenanceWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    companion object {
        // Constants for keys used in the worker
        const val KEY_RESULT = "maintenance_result"
        const val PREFS_NAME = "periodic_work_prefs"
        const val KEY_LAST_RUN = "last_run_time"
        const val KEY_RUN_COUNT = "run_count"
    }

    private val prefs: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Creates a notification to show when the work is running in the foreground
     */
    private fun createForegroundInfo(): ForegroundInfo {
        val notification = NotificationCompat.Builder(applicationContext, App.CHANNEL_BASIC_ID)
            .setContentTitle(applicationContext.getString(R.string.sync_notification_title))
            .setContentText(applicationContext.getString(R.string.sync_notification_content))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .build()

        return ForegroundInfo(applicationContext.resources.getInteger(R.integer.maintenance_notification_id), notification)
    }

    override suspend fun doWork(): Result {
        try {
            // Create a foreground notification
            setForeground(createForegroundInfo())

            // Simulate maintenance tasks
            val dateFormat = SimpleDateFormat(applicationContext.getString(R.string.date_format_pattern), Locale.getDefault())
            val currentTime = dateFormat.format(Date())
            
            // Increment the execution counter
            val runCount = prefs.getInt(KEY_RUN_COUNT, 0) + 1
            
            // Save information about this execution
            prefs.edit {
                putString(KEY_LAST_RUN, currentTime)
                putInt(KEY_RUN_COUNT, runCount)
            }
            
            // Simulate different maintenance tasks based on the counter
            val taskDescription = when {
                runCount % 4 == 0 -> applicationContext.getString(R.string.maintenance_task_cache)
                runCount % 4 == 1 -> applicationContext.getString(R.string.maintenance_task_index)
                runCount % 4 == 2 -> applicationContext.getString(R.string.maintenance_task_integrity)
                else -> applicationContext.getString(R.string.maintenance_task_sync)
            }
            
            // Return a successful result with information about the completed task
            return Result.success(
                workDataOf(
                    KEY_RESULT to taskDescription,
                    KEY_LAST_RUN to currentTime,
                    KEY_RUN_COUNT to runCount
                )
            )
        } catch (e: Exception) {
            return Result.failure(
                workDataOf(KEY_RESULT to applicationContext.getString(R.string.maintenance_error, e.message))
            )
        }
    }
} 