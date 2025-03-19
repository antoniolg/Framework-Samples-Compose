package com.antonioleiva.frameworksamples.ui.screens.workmanager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.ui.components.Screen
import kotlinx.serialization.Serializable

@Serializable
object BasicSyncScreen

@Composable
fun BasicSyncScreen(onBack: () -> Unit) {
    val workState = rememberWorkState()

    Screen(
        title = stringResource(R.string.work_manager_basic_sync),
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
                text = stringResource(R.string.basic_sync_instructions),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            
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
                onClick = { workState.startWork() },
                enabled = !workState.isWorkRunning,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.start_work))
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
