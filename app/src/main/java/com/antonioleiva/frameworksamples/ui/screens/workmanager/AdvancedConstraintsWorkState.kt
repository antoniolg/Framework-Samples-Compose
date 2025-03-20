package com.antonioleiva.frameworksamples.ui.screens.workmanager

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.ui.screens.workmanager.workers.ConstrainedWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.util.UUID
import java.util.concurrent.TimeUnit

@Serializable
object AdvancedConstraintsWorkScreen

@Composable
fun rememberAdvancedConstraintsWorkState(
    context: Context = LocalContext.current,
    initialStatus: String = stringResource(R.string.work_not_started),
    workManager: WorkManager = remember { WorkManager.getInstance(context) },
    scope: CoroutineScope = rememberCoroutineScope(),
    initialBackoffDelay: Int = integerResource(id = R.integer.initial_backoff_delay)
) = remember(context, workManager, scope) {
    AdvancedConstraintsWorkState(context, initialStatus, workManager, scope, initialBackoffDelay)
}

class AdvancedConstraintsWorkState(
    val context: Context,
    initialStatus: String,
    val workManager: WorkManager,
    val scope: CoroutineScope,
    val initialBackoffDelay: Int
) {
    var workId by mutableStateOf<UUID?>(null)
    var workResult by mutableStateOf<String?>(null)
    var isWorkRunning by mutableStateOf(false)
    var workStatus by mutableStateOf(initialStatus)
    var attemptCount by mutableStateOf(0)
    
    // Network type options
    var networkTypeNotRequired by mutableStateOf(false)
    var networkTypeConnected by mutableStateOf(true)
    var networkTypeUnmetered by mutableStateOf(false)
    
    // Constraint options
    var requiresBatteryNotLow by mutableStateOf(true)
    var requiresCharging by mutableStateOf(false)
    var requiresStorageNotLow by mutableStateOf(true)
    
    // Backoff policy
    var useLinearBackoff by mutableStateOf(true)
    var useExponentialBackoff by mutableStateOf(false)

    fun startConstrainedWork() {
        // Build constraints based on user selections
        val constraintsBuilder = Constraints.Builder()
        
        // Configure the required network type
        val networkType = when {
            networkTypeNotRequired -> NetworkType.NOT_REQUIRED
            networkTypeConnected -> NetworkType.CONNECTED
            networkTypeUnmetered -> NetworkType.UNMETERED
            else -> NetworkType.CONNECTED
        }
        constraintsBuilder.setRequiredNetworkType(networkType)
        
        // Configure other constraints
        if (requiresBatteryNotLow) {
            constraintsBuilder.setRequiresBatteryNotLow(true)
        }
        
        if (requiresCharging) {
            constraintsBuilder.setRequiresCharging(true)
        }
        
        if (requiresStorageNotLow) {
            constraintsBuilder.setRequiresStorageNotLow(true)
        }
        
        // Build the final constraints
        val constraints = constraintsBuilder.build()
        
        // Configure the retry policy
        val backoffPolicy = if (useLinearBackoff) {
            BackoffPolicy.LINEAR
        } else {
            BackoffPolicy.EXPONENTIAL
        }
        
        // Create a work request with constraints and retry policy
        val constrainedWorkRequest = OneTimeWorkRequestBuilder<ConstrainedWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(
                backoffPolicy,
                initialBackoffDelay.toLong(),
                TimeUnit.SECONDS
            )
            .build()
        
        // Enqueue the work to WorkManager
        workManager.enqueue(constrainedWorkRequest)
        
        // Update the state
        workId = constrainedWorkRequest.id
        workStatus = context.getString(R.string.work_enqueued)
        isWorkRunning = true
        attemptCount = 0
        workResult = null
        
        // Observe the work status
        observeWorkStatus()
    }

    fun cancelConstrainedWork() {
        workManager.cancelAllWork()
        workStatus = context.getString(R.string.work_cancelled)
        isWorkRunning = false
    }

    private fun observeWorkStatus() {
        workId?.let { id ->
            val workInfoFlow = workManager.getWorkInfoByIdFlow(id)
            scope.launch {
                workInfoFlow.collect { workInfo ->
                    workInfo?.let { updateWorkInfo(it) }
                }
            }
        }
    }

    private fun updateWorkInfo(workInfo: WorkInfo) {
        when (workInfo.state) {
            WorkInfo.State.ENQUEUED -> {
                workStatus = context.getString(R.string.work_enqueued)
            }
            WorkInfo.State.RUNNING -> {
                workStatus = context.getString(R.string.work_running)
                
                // Update the number of attempts
                attemptCount = workInfo.runAttemptCount
            }
            WorkInfo.State.SUCCEEDED -> {
                workStatus = context.getString(R.string.work_succeeded)
                isWorkRunning = false
                
                // Show the work result
                val result = workInfo.outputData.getString(ConstrainedWorker.KEY_RESULT)
                attemptCount = workInfo.outputData.getInt(ConstrainedWorker.KEY_ATTEMPT, 0)
                
                if (result != null) {
                    workResult = result
                }
            }
            WorkInfo.State.FAILED -> {
                workStatus = context.getString(R.string.work_failed)
                isWorkRunning = false
                
                // Show the error
                val result = workInfo.outputData.getString(ConstrainedWorker.KEY_RESULT)
                attemptCount = workInfo.outputData.getInt(ConstrainedWorker.KEY_ATTEMPT, 0)
                
                if (result != null) {
                    workResult = result
                }
            }
            WorkInfo.State.BLOCKED -> {
                workStatus = context.getString(R.string.work_blocked)
            }
            WorkInfo.State.CANCELLED -> {
                workStatus = context.getString(R.string.work_cancelled)
                isWorkRunning = false
            }
        }
    }
} 