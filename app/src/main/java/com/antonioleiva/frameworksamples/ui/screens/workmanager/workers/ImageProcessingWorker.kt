package com.antonioleiva.frameworksamples.ui.screens.workmanager.workers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import androidx.core.graphics.createBitmap
import androidx.core.graphics.get
import androidx.core.graphics.set
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.antonioleiva.frameworksamples.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * Worker that simulates image processing.
 * Receives the URI of an image, applies a simple filter (grayscale)
 * and saves the processed image to internal storage.
 */
class ImageProcessingWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    companion object {
        const val KEY_IMAGE_URI = "image_uri"
        const val KEY_RESULT_IMAGE_URI = "result_image_uri"
        const val KEY_PROCESSING_TIME = "processing_time"
        const val KEY_ERROR = "error"
        const val KEY_PROGRESS = "progress"
    }

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                // Get the URI of the input image
                val imageUriString = inputData.getString(KEY_IMAGE_URI)
                    ?: return@withContext Result.failure(
                        workDataOf(KEY_ERROR to applicationContext.getString(R.string.image_no_uri_error))
                    )

                val imageUri = imageUriString.toUri()
                
                // Open the image stream from the URI
                val inputStream = applicationContext.contentResolver.openInputStream(imageUri)
                    ?: return@withContext Result.failure(
                        workDataOf(KEY_ERROR to applicationContext.getString(R.string.image_open_error))
                    )

                // Decode the image
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()

                // Update progress
                setProgress(workDataOf(KEY_PROGRESS to applicationContext.getString(R.string.image_processing_progress)))
                
                // Simulate processing time
                delay(2000)

                // Apply grayscale filter
                val grayScaleBitmap = applyGrayscaleFilter(bitmap)
                
                // Save the processed image
                val outputFile = File(applicationContext.cacheDir, "processed_image_${System.currentTimeMillis()}.jpg")
                val outputStream = FileOutputStream(outputFile)
                grayScaleBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                outputStream.close()

                // Create URI for the processed image
                val resultUri = Uri.fromFile(outputFile)

                // Return successful result with the URI of the processed image
                Result.success(
                    workDataOf(
                        KEY_RESULT_IMAGE_URI to resultUri.toString(),
                        KEY_PROCESSING_TIME to applicationContext.getString(R.string.image_processing_completed)
                    )
                )
            } catch (e: Exception) {
                Result.failure(workDataOf(KEY_ERROR to applicationContext.getString(R.string.image_processing_error, e.message)))
            }
        }
    }

    private fun applyGrayscaleFilter(source: Bitmap): Bitmap {
        val width = source.width
        val height = source.height
        val result = createBitmap(width, height, Bitmap.Config.ARGB_8888)

        for (x in 0 until width) {
            for (y in 0 until height) {
                val pixel = source[x, y]
                val red = Color.red(pixel)
                val green = Color.green(pixel)
                val blue = Color.blue(pixel)
                
                // Formula for grayscale
                val gray = (0.299 * red + 0.587 * green + 0.114 * blue).toInt()
                val grayPixel = Color.rgb(gray, gray, gray)

                result[x, y] = grayPixel
            }
        }
        
        return result
    }
}
