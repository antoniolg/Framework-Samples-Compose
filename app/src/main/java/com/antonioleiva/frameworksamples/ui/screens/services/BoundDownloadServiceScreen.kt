package com.antonioleiva.frameworksamples.ui.screens.services

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.ui.components.Screen
import kotlinx.serialization.Serializable

@Serializable
object BoundDownloadServiceScreen

@Composable
fun BoundDownloadServiceScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var url by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var isRunning by remember { mutableStateOf(false) }
    var downloadService by remember { mutableStateOf<BoundDownloadService?>(null) }
    var isBound by remember { mutableStateOf(false) }

    val serviceConnection = remember {
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as BoundDownloadService.DownloadBinder
                downloadService = binder.getService()
                isBound = true

                downloadService?.setProgressListener { progress, url ->
                    status = context.getString(R.string.download_progress, url, progress)
                    if (progress == 100) {
                        isRunning = false
                    }
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                downloadService = null
                isBound = false
            }
        }
    }

    DisposableEffect(context) {
        val intent = Intent(context, BoundDownloadService::class.java)
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

        onDispose {
            if (isBound) {
                downloadService?.removeProgressListener()
                context.unbindService(serviceConnection)
                isBound = false
            }
        }
    }

    Screen(
        title = stringResource(R.string.bound_download_service_title),
        onBack = onBack
    ) { modifier ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = url,
                onValueChange = { url = it },
                label = { Text(stringResource(R.string.url_hint)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedButton(
                onClick = {
                    val downloadUrl = url.takeIf { it.isNotEmpty() }
                        ?: "https://ejemplo.com/archivo.zip"

                    downloadService?.startDownload(downloadUrl)
                    isRunning = true
                },
                enabled = !isRunning,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(stringResource(R.string.start_download))
            }

            OutlinedButton(
                onClick = {
                    downloadService?.stopDownload()
                    status = context.getString(R.string.download_stopped)
                    isRunning = false
                },
                enabled = isRunning,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(stringResource(R.string.stop_download))
            }

            if (status.isNotEmpty()) {
                Text(
                    text = status,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            Text(
                text = stringResource(R.string.bound_download_service_instructions),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
} 