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
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.ui.screens.workmanager.workers.SyncWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun rememberWorkState(
    context: Context = LocalContext.current,
    initialStatus: String = stringResource(R.string.work_not_started),
    workManager: WorkManager = remember { WorkManager.getInstance(context) },
    scope: CoroutineScope = rememberCoroutineScope()
) = remember(context, workManager, scope) {
    WorkState(context, initialStatus, workManager, scope)
}

class WorkState(
    val context: Context,
    initialStatus: String,
    val workManager: WorkManager,
    val scope: CoroutineScope
) {
    var workId by mutableStateOf<UUID?>(null)
    var workResult by mutableStateOf<String?>(null)
    var isWorkRunning by mutableStateOf(false)
    var workStatus by mutableStateOf(initialStatus)

    fun startWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncWorkRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .build()

        workManager.enqueue(syncWorkRequest)

        workId = syncWorkRequest.id
        workStatus = context.getString(R.string.work_enqueued)
        isWorkRunning = true
        workResult = null

        observeWorkStatus()
    }

    fun cancelWork() {
        workId?.let {
            workManager.cancelWorkById(it)
            workStatus = context.getString(R.string.work_cancelled)
            isWorkRunning = false
        }
    }

    private fun observeWorkStatus() {
        workId?.let { id ->
            val workInfoFlow = workManager.getWorkInfoByIdFlow(id)
            scope.launch {
                workInfoFlow.collect {
                    it?.let(::updateFromWorkInfo)
                }
            }
        }
    }

    fun updateFromWorkInfo(info: WorkInfo) {
        when (info.state) {
            WorkInfo.State.ENQUEUED -> {
                workStatus = context.getString(R.string.work_enqueued)
                isWorkRunning = true
            }

            WorkInfo.State.RUNNING -> {
                workStatus = context.getString(R.string.work_running)
                isWorkRunning = true
            }

            WorkInfo.State.SUCCEEDED -> {
                workStatus = context.getString(R.string.work_succeeded)
                isWorkRunning = false
                workResult = info.outputData.getString(SyncWorker.KEY_RESULT)
            }

            WorkInfo.State.FAILED -> {
                workStatus = context.getString(R.string.work_failed)
                isWorkRunning = false
                workResult = info.outputData.getString(SyncWorker.KEY_RESULT)
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