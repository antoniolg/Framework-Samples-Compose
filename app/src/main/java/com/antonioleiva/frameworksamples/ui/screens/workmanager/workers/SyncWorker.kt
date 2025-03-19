package com.antonioleiva.frameworksamples.ui.screens.workmanager.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.Data
import com.antonioleiva.frameworksamples.R
import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * Worker que simula sincronización con un servidor remoto.
 * Para propósitos de demostración, simplemente espera un tiempo aleatorio
 * y luego retorna un resultado exitoso o fallido basado en una probabilidad.
 */
class SyncWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    companion object {
        const val KEY_RESULT = "sync_result"
    }

    override suspend fun doWork(): Result {
        try {
            // Establecer progreso
            setProgress(Data.Builder().putString("progress", applicationContext.getString(R.string.sync_progress)).build())
            
            // Simular una operación de red que toma entre 2 y 5 segundos
            val syncTime = Random.nextInt(2000, 5000).toLong()
            delay(syncTime)

            // Simular un resultado exitoso con una probabilidad del 80%
            val isSuccessful = Random.nextDouble() < 0.8

            return if (isSuccessful) {
                // Simular una sincronización exitosa
                Result.success(Data.Builder().putString(KEY_RESULT, applicationContext.getString(R.string.sync_success)).build())
            } else {
                // Simular un error de sincronización
                Result.failure(Data.Builder().putString(KEY_RESULT, applicationContext.getString(R.string.sync_failure)).build())
            }
        } catch (e: Exception) {
            // En caso de excepción, retornar un error
            return Result.failure(Data.Builder().putString(KEY_RESULT, applicationContext.getString(R.string.sync_error, e.message)).build())
        }
    }
}
