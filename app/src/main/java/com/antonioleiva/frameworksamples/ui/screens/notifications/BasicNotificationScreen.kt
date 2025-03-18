package com.antonioleiva.frameworksamples.ui.screens.notifications

import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.antonioleiva.frameworksamples.App.Companion.CHANNEL_BASIC_ID
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.ui.components.Screen
import kotlinx.serialization.Serializable

@Serializable
object BasicNotificationScreen

@Composable
fun BasicNotificationScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showNotification(context)
        }
    }

    Screen(
        title = stringResource(R.string.notification_basic),
        onBack = onBack
    ) { modifier ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    checkPermissionAndShowNotification(context, launcher)
                }
            ) {
                Text(stringResource(R.string.show_notification))
            }

            Text(
                text = stringResource(R.string.notification_basic_instructions),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

private fun checkPermissionAndShowNotification(
    context: Context,
    launcher: ActivityResultLauncher<String>
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        when {
            ContextCompat.checkSelfPermission(context, POST_NOTIFICATIONS) == PERMISSION_GRANTED ->
                showNotification(context)
            else -> launcher.launch(POST_NOTIFICATIONS)
        }
    } else {
        showNotification(context)
    }
}

@SuppressLint("MissingPermission")
private fun showNotification(context: Context) {
    val builder = NotificationCompat.Builder(context, CHANNEL_BASIC_ID)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle(context.getString(R.string.notification_basic_title))
        .setContentText(context.getString(R.string.notification_basic_content))
        .setStyle(
            NotificationCompat.BigTextStyle()
                .bigText(context.getString(R.string.notification_expandable_content))
        )
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    NotificationManagerCompat.from(context).notify(1, builder.build())
} 