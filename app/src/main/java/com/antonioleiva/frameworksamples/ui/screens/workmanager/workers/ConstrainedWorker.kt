package com.antonioleiva.frameworksamples.ui.screens.workmanager.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.antonioleiva.frameworksamples.R
import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * Worker that demonstrates the use of advanced constraints.
 * Simulates a job that can randomly fail to demonstrate
 * retry policies.
 */
class ConstrainedWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    companion object {
        const val KEY_RESULT = "constrained_result"
        const val KEY_ATTEMPT = "attempt_number"
    }

    override suspend fun doWork(): Result {
        try {
            // Get the current attempt number
            val attemptNumber = runAttemptCount
            
            // Update progress
            setProgress(workDataOf(
                "progress" to applicationContext.getString(R.string.constrained_progress, attemptNumber),
                KEY_ATTEMPT to attemptNumber
            ))
            
            // Simulate a task that takes between 2 and 4 seconds
            val workTime = Random.nextInt(2000, 4000).toLong()
            delay(workTime)
            
            // Simulate a random failure in the first attempts to demonstrate the retry policy
            // The probability of success increases with each attempt
            val successProbability = when {
                attemptNumber <= 1 -> 0.3  // 30% success rate on first attempt
                attemptNumber == 2 -> 0.6  // 60% success rate on second attempt
                else -> 0.9               // 90% success rate on subsequent attempts
            }
            
            val isSuccessful = Random.nextDouble() < successProbability
            
            return if (isSuccessful) {
                // Work completed successfully
                Result.success(workDataOf(
                    KEY_RESULT to applicationContext.resources.getQuantityString(R.plurals.constrained_success, attemptNumber, attemptNumber),
                    KEY_ATTEMPT to attemptNumber
                ))
            } else {
                // Simulate a failure to demonstrate the retry policy
                Result.retry()
            }
        } catch (e: Exception) {
            return Result.failure(workDataOf(
                KEY_RESULT to applicationContext.getString(R.string.constrained_error, e.message),
                KEY_ATTEMPT to runAttemptCount
            ))
        }
    }
} 