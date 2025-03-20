package com.antonioleiva.frameworksamples.ui.screens.workmanager

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.ui.components.Screen
import kotlinx.serialization.Serializable

@Serializable
object ImageProcessingScreen

@Composable
fun ImageProcessingScreen(onBack: () -> Unit) {
    val workState = rememberImageProcessingWorkState()
    
    // Image picker launcher
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            workState.setSelectedImage(it)
        }
    }

    Screen(
        title = stringResource(R.string.work_manager_image_processing),
        onBack = onBack
    ) { paddingModifier ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .then(paddingModifier),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.image_processing_instructions),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            
            // Display the selected or processed image
            val imageUri = workState.resultImageUri ?: workState.selectedImageUri
            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )
            }
            
            Text(
                text = stringResource(R.string.work_result, workState.workStatus),
                style = MaterialTheme.typography.titleMedium
            )
            
            if (workState.workResult != null) {
                Text(
                    text = workState.workResult!!,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { 
                    pickImageLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.select_image))
            }
            
            Button(
                onClick = { workState.processImage() },
                enabled = workState.selectedImageUri != null && !workState.isWorkRunning,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.process_image))
            }

            Button(
                onClick = { workState.cancelWork() },
                enabled = workState.isWorkRunning,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.cancel_work))
            }
        }
    }
}
