package com.antonioleiva.frameworksamples.ui.screens.workmanager

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.ui.screens.workmanager.workers.WorkerA
import com.antonioleiva.frameworksamples.ui.screens.workmanager.workers.WorkerB
import com.antonioleiva.frameworksamples.ui.screens.workmanager.workers.WorkerC
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun rememberChainedWorkState(
    context: Context = LocalContext.current,
    initialStatus: String = stringResource(R.string.work_not_started),
    workManager: WorkManager = remember { WorkManager.getInstance(context) },
    scope: CoroutineScope = rememberCoroutineScope()
) = remember(context, workManager, scope) {
    ChainedWorkState(context, initialStatus, workManager, scope)
}

class ChainedWorkState(
    val context: Context,
    initialStatus: String,
    val workManager: WorkManager,
    val scope: CoroutineScope
) {
    var workAStatus by mutableStateOf(initialStatus)
    var workBStatus by mutableStateOf(initialStatus)
    var workCStatus by mutableStateOf(initialStatus)
    var finalResult by mutableStateOf<String?>(null)
    var isWorkRunning by mutableStateOf(false)
    var isSequential by mutableStateOf(true)
    
    // Work IDs
    private var workAId: UUID? = null
    private var workBId: UUID? = null
    private var workCId: UUID? = null

    fun startChainedWork() {
        // Reset statuses
        workAStatus = context.getString(R.string.work_not_started)
        workBStatus = context.getString(R.string.work_not_started)
        workCStatus = context.getString(R.string.work_not_started)
        finalResult = null
        isWorkRunning = true
        
        // Create individual work requests
        val workRequestA = OneTimeWorkRequestBuilder<WorkerA>().build()
        val workRequestB = OneTimeWorkRequestBuilder<WorkerB>().build()
        val workRequestC = OneTimeWorkRequestBuilder<WorkerC>().build()
        
        workAId = workRequestA.id
        workBId = workRequestB.id
        workCId = workRequestC.id
        
        // Observe work statuses
        observeWorkStatus(workAId!!, WorkerType.A)
        observeWorkStatus(workBId!!, WorkerType.B)
        observeWorkStatus(workCId!!, WorkerType.C)
        
        // Determine the selected chain type
        if (isSequential) {
            // Sequential chaining: A → B → C
            workManager
                .beginWith(workRequestA)
                .then(workRequestB)
                .then(workRequestC)
                .enqueue()
        } else {
            // Parallel and sequential chaining: A,B → C
            workManager
                .beginWith(listOf(workRequestA, workRequestB))
                .then(workRequestC)
                .enqueue()
        }
    }

    fun cancelChainedWork() {
        workManager.cancelAllWork()
        workAStatus = context.getString(R.string.work_cancelled)
        workBStatus = context.getString(R.string.work_cancelled)
        workCStatus = context.getString(R.string.work_cancelled)
        isWorkRunning = false
        finalResult = null
    }

    private fun observeWorkStatus(workId: UUID, workerType: WorkerType) {
        val workInfoFlow = workManager.getWorkInfoByIdFlow(workId)
        scope.launch {
            workInfoFlow.collectLatest { workInfo ->
                if (workInfo != null) {
                    updateWorkStatus(workInfo, workerType)
                    
                    // If work C has finished, show the final result
                    if (workerType == WorkerType.C && workInfo.state == WorkInfo.State.SUCCEEDED) {
                        finalResult = workInfo.outputData.getString(WorkerC.KEY_FINAL_RESULT)
                        isWorkRunning = false
                    }
                }
            }
        }
    }

    private fun updateWorkStatus(workInfo: WorkInfo, workerType: WorkerType) {
        val status = when (workInfo.state) {
            WorkInfo.State.ENQUEUED -> context.getString(R.string.work_enqueued)
            WorkInfo.State.RUNNING -> context.getString(R.string.work_running)
            WorkInfo.State.SUCCEEDED -> context.getString(R.string.work_succeeded)
            WorkInfo.State.FAILED -> context.getString(R.string.work_failed)
            WorkInfo.State.BLOCKED -> context.getString(R.string.work_blocked)
            WorkInfo.State.CANCELLED -> context.getString(R.string.work_cancelled)
        }
        
        when (workerType) {
            WorkerType.A -> workAStatus = context.getString(
                R.string.work_status_format, 
                context.getString(R.string.worker_id_a), 
                status
            )
            WorkerType.B -> workBStatus = context.getString(
                R.string.work_status_format, 
                context.getString(R.string.worker_id_b), 
                status
            )
            WorkerType.C -> workCStatus = context.getString(
                R.string.work_status_format, 
                context.getString(R.string.worker_id_c), 
                status
            )
        }
    }

    enum class WorkerType { A, B, C }
} 