package com.antonioleiva.frameworksamples.ui.screens.workmanager

import android.content.Context
import android.net.Uri
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
import androidx.work.workDataOf
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.ui.screens.workmanager.workers.ImageProcessingWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.UUID
import androidx.core.net.toUri

@Composable
fun rememberImageProcessingWorkState(
    context: Context = LocalContext.current,
    initialStatus: String = stringResource(R.string.work_not_started),
    workManager: WorkManager = remember { WorkManager.getInstance(context) },
    scope: CoroutineScope = rememberCoroutineScope()
) = remember(context, workManager, scope) {
    ImageProcessingWorkState(context, initialStatus, workManager, scope)
}

class ImageProcessingWorkState(
    val context: Context,
    initialStatus: String,
    val workManager: WorkManager,
    val scope: CoroutineScope
) {
    var workId by mutableStateOf<UUID?>(null)
    var workResult by mutableStateOf<String?>(null)
    var isWorkRunning by mutableStateOf(false)
    var workStatus by mutableStateOf(initialStatus)
    var selectedImageUri by mutableStateOf<Uri?>(null)
    var resultImageUri by mutableStateOf<Uri?>(null)

    fun setSelectedImage(uri: Uri) {
        selectedImageUri = uri
    }

    fun processImage() {
        selectedImageUri?.let { uri ->
            // Create input data for the Worker
            val inputData = workDataOf(ImageProcessingWorker.KEY_IMAGE_URI to uri.toString())
            
            // Create a one-time work request
            val imageProcessingRequest = OneTimeWorkRequestBuilder<ImageProcessingWorker>()
                .setInputData(inputData)
                .build()
            
            // Enqueue the work to WorkManager
            workManager.enqueue(imageProcessingRequest)
            
            // Update the UI
            workId = imageProcessingRequest.id
            workStatus = context.getString(R.string.work_enqueued)
            isWorkRunning = true
            workResult = null
            resultImageUri = null
            
            observeWorkStatus()
        }
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

    private fun updateFromWorkInfo(info: WorkInfo) {
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
                
                // Get the processed image URI
                val resultUriString = info.outputData.getString(ImageProcessingWorker.KEY_RESULT_IMAGE_URI)
                if (resultUriString != null) {
                    resultImageUri = resultUriString.toUri()
                }
                
                // Get the processing time
                val processingTime = info.outputData.getString(ImageProcessingWorker.KEY_PROCESSING_TIME)
                if (processingTime != null) {
                    workResult = processingTime
                }
            }

            WorkInfo.State.FAILED -> {
                workStatus = context.getString(R.string.work_failed)
                isWorkRunning = false
                
                // Get the error message
                val error = info.outputData.getString(ImageProcessingWorker.KEY_ERROR)
                if (error != null) {
                    workResult = error
                }
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
