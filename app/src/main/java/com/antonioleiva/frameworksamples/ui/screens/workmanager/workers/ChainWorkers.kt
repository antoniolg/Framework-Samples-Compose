package com.antonioleiva.frameworksamples.ui.screens.workmanager.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.antonioleiva.frameworksamples.R
import kotlinx.coroutines.delay

/**
 * Worker A for the chained work example.
 * Simulates the first stage of a process, such as data download.
 */
class WorkerA(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    
    companion object {
        const val KEY_RESULT = "result_a"
        const val KEY_DATA = "data_a"
    }

    override suspend fun doWork(): Result {
        try {
            // Simulate a task that takes 2 seconds
            setProgress(workDataOf("progress" to applicationContext.getString(R.string.worker_a_progress)))
            delay(2000)
            
            // Generate data to pass to the next Worker
            val data = applicationContext.getString(R.string.worker_a_data, System.currentTimeMillis().toString())
            
            // Return success and the generated data
            return Result.success(workDataOf(
                KEY_RESULT to applicationContext.getString(R.string.worker_a_success),
                KEY_DATA to data
            ))
        } catch (e: Exception) {
            return Result.failure(workDataOf("error" to applicationContext.getString(R.string.worker_a_error, e.message)))
        }
    }
}

/**
 * Worker B for the chained work example.
 * Simulates the second stage of a process, such as data processing.
 */
class WorkerB(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    
    companion object {
        const val KEY_RESULT = "result_b"
        const val KEY_DATA = "data_b"
    }

    override suspend fun doWork(): Result {
        try {
            // Get data from the previous Worker if available
            val inputData = inputData.getString(WorkerA.KEY_DATA) ?: applicationContext.getString(R.string.worker_b_no_input)
            
            // Simulate a task that takes 3 seconds
            setProgress(workDataOf("progress" to applicationContext.getString(R.string.worker_b_progress)))
            delay(3000)
            
            // Process the data and generate new data
            val processedData = applicationContext.getString(R.string.worker_b_data, inputData)
            
            // Return success and the processed data
            return Result.success(workDataOf(
                KEY_RESULT to applicationContext.getString(R.string.worker_b_success),
                KEY_DATA to processedData
            ))
        } catch (e: Exception) {
            return Result.failure(workDataOf("error" to applicationContext.getString(R.string.worker_b_error, e.message)))
        }
    }
}

/**
 * Worker C for the chained work example.
 * Simulates the final stage of a process, such as saving results.
 */
class WorkerC(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    
    companion object {
        const val KEY_RESULT = "result_c"
        const val KEY_FINAL_RESULT = "final_result"
    }

    override suspend fun doWork(): Result {
        try {
            // Get data from previous Workers
            val dataFromB = inputData.getString(WorkerB.KEY_DATA) ?: applicationContext.getString(R.string.worker_c_no_input)
            
            // Simulate a task that takes 2 seconds
            setProgress(workDataOf("progress" to applicationContext.getString(R.string.worker_c_progress)))
            delay(2000)
            
            // Generate the final result
            val finalResult = applicationContext.getString(R.string.worker_c_data, dataFromB)
            
            // Return success and the final result
            return Result.success(workDataOf(
                KEY_RESULT to applicationContext.getString(R.string.worker_c_success),
                KEY_FINAL_RESULT to finalResult
            ))
        } catch (e: Exception) {
            return Result.failure(workDataOf("error" to applicationContext.getString(R.string.worker_c_error, e.message)))
        }
    }
} 