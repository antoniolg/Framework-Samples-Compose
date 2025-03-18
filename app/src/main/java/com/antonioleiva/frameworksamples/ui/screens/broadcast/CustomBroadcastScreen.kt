package com.antonioleiva.frameworksamples.ui.screens.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.ui.components.Screen
import kotlinx.serialization.Serializable

@Serializable
object CustomBroadcastScreen

@Composable
fun CustomBroadcastScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var message by remember { mutableStateOf("") }
    var receivedMessage by remember { mutableStateOf("") }

    DisposableEffect(context) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val received = intent.getStringExtra(EXTRA_MESSAGE) ?: return
                receivedMessage = received
            }
        }

        ContextCompat.registerReceiver(
            context,
            receiver,
            IntentFilter(ACTION_CUSTOM_BROADCAST),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    Screen(
        title = stringResource(R.string.custom_broadcast_sample_title),
        onBack = onBack
    ) { modifier ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text(stringResource(R.string.message_hint)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedButton(
                onClick = {
                    if (message.isNotEmpty()) {
                        context.sendBroadcast(
                            Intent(ACTION_CUSTOM_BROADCAST)
                                .setPackage(context.packageName)
                                .putExtra(EXTRA_MESSAGE, message)
                        )
                        message = ""
                    }
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(stringResource(R.string.send_broadcast))
            }

            if (receivedMessage.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.received_message, receivedMessage),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            Text(
                text = stringResource(R.string.custom_broadcast_sample_instructions),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

private const val ACTION_CUSTOM_BROADCAST = "com.antonioleiva.frameworksamples.CUSTOM_BROADCAST"
private const val EXTRA_MESSAGE = "message" 