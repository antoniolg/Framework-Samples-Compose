package com.antonioleiva.frameworksamples.ui.screens.workmanager

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.edit
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.ui.screens.workmanager.workers.PeriodicMaintenanceWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

data class PeriodicWorkData(
    val lastRun: String = "Nunca",
    val runCount: Int = 0
)

@Composable
fun rememberPeriodicWorkState(
    context: Context = LocalContext.current,
    initialStatus: String = stringResource(R.string.work_not_started),
    workManager: WorkManager = remember { WorkManager.getInstance(context) },
    scope: CoroutineScope = rememberCoroutineScope()
) = remember(context, workManager, scope) {
    PeriodicWorkState(context, initialStatus, workManager, scope)
}

class PeriodicWorkState(
    val context: Context,
    initialStatus: String,
    val workManager: WorkManager,
    val scope: CoroutineScope
) {
    companion object {
        const val WORK_NAME = "periodic_maintenance_work"
        const val KEY_LAST_SCHEDULED = "last_scheduled"
    }

    var workStatus by mutableStateOf(initialStatus)
    var isWorkRunning by mutableStateOf(false)
    
    private val _workData = MutableStateFlow(PeriodicWorkData())
    val workData: StateFlow<PeriodicWorkData> = _workData.asStateFlow()
    
    var selectedIntervalMinutes by mutableIntStateOf(15)
    var selectedPolicyKeep by mutableStateOf(true)
    
    init {
        loadSavedWorkInfo()
        observeWorkStatus()
    }
    
    fun loadSavedWorkInfo() {
        val prefs = context.getSharedPreferences(PeriodicMaintenanceWorker.PREFS_NAME, Context.MODE_PRIVATE)
        val lastRun = prefs.getString(PeriodicMaintenanceWorker.KEY_LAST_RUN, "Nunca") ?: "Nunca"
        val runCount = prefs.getInt(PeriodicMaintenanceWorker.KEY_RUN_COUNT, 0)
        
        _workData.value = PeriodicWorkData(lastRun, runCount)
    }

    fun schedulePeriodicWork() {
        val existingWorkPolicy = if (selectedPolicyKeep) {
            ExistingPeriodicWorkPolicy.KEEP
        } else {
            ExistingPeriodicWorkPolicy.UPDATE
        }
        
        // Create a periodic work request
        val periodicWorkRequest = PeriodicWorkRequestBuilder<PeriodicMaintenanceWorker>(
            selectedIntervalMinutes.toLong(), TimeUnit.MINUTES
        ).build()
        
        // Schedule the work with the selected policy
        workManager.enqueueUniquePeriodicWork(
            WORK_NAME,
            existingWorkPolicy,
            periodicWorkRequest
        )
        
        // Update the UI
        workStatus = context.getString(R.string.work_enqueued)
        isWorkRunning = true
        
        // Save the current time as the scheduling time
        val dateFormat = SimpleDateFormat(context.getString(R.string.date_format_pattern), Locale.getDefault())
        val currentTime = dateFormat.format(Date())
        context.getSharedPreferences(PeriodicMaintenanceWorker.PREFS_NAME, Context.MODE_PRIVATE)
            .edit {
                putString(KEY_LAST_SCHEDULED, currentTime)
            }
    }

    fun cancelPeriodicWork() {
        workManager.cancelUniqueWork(WORK_NAME)
        workStatus = context.getString(R.string.work_cancelled)
        isWorkRunning = false
    }
    
    private fun observeWorkStatus() {
        val workInfoFlow = workManager.getWorkInfosForUniqueWorkFlow(WORK_NAME)
        scope.launch {
            workInfoFlow.collect { workInfoList ->
                if (workInfoList.isNotEmpty()) {
                    val workInfo = workInfoList[0]
                    updateWorkStatus(workInfo)
                } else {
                    workStatus = context.getString(R.string.work_not_started)
                    isWorkRunning = false
                }
            }
        }
    }
    
    private fun updateWorkStatus(workInfo: WorkInfo) {
        when (workInfo.state) {
            WorkInfo.State.ENQUEUED -> {
                workStatus = context.getString(R.string.work_enqueued)
                isWorkRunning = true
            }
            WorkInfo.State.RUNNING -> {
                workStatus = context.getString(R.string.work_running)
                isWorkRunning = true
                // Refresh the data in case it changed during execution
                loadSavedWorkInfo()
            }
            WorkInfo.State.SUCCEEDED -> {
                workStatus = context.getString(R.string.work_succeeded)
                isWorkRunning = true
                // Update the information of the last execution
                loadSavedWorkInfo()
            }
            WorkInfo.State.FAILED -> {
                workStatus = context.getString(R.string.work_failed)
                isWorkRunning = false
            }
            WorkInfo.State.BLOCKED -> {
                workStatus = context.getString(R.string.work_blocked)
                isWorkRunning = true
            }
            WorkInfo.State.CANCELLED -> {
                workStatus = context.getString(R.string.work_cancelled)
                isWorkRunning = false
            }
        }
    }
} 