package com.antonioleiva.frameworksamples.ui.screens.workmanager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.ui.components.Screen

@Composable
fun AdvancedConstraintsWorkScreen(onBack: () -> Unit) {
    val state = rememberAdvancedConstraintsWorkState()
    
    Screen(
        title = stringResource(R.string.work_manager_advanced_constraints),
        onBack = onBack
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Explanation text
            Text(
                text = stringResource(R.string.advanced_constraints_explanation),
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Constraints Card
            ConstraintsCard(
                state = state,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Status Card
            StatusCard(
                state = state,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Action Buttons
            Button(
                onClick = { state.startConstrainedWork() },
                enabled = !state.isWorkRunning,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.start_work))
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = { state.cancelConstrainedWork() },
                enabled = state.isWorkRunning,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.cancel_work))
            }
        }
    }
}

@Composable
private fun ConstraintsCard(
    state: AdvancedConstraintsWorkState,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Title
            Text(
                text = stringResource(R.string.work_constraints),
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Network Type
            Text(
                text = stringResource(R.string.network_type_required),
                style = MaterialTheme.typography.bodyMedium
            )
            
            Column(
                modifier = Modifier
                    .selectableGroup()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = state.networkTypeNotRequired,
                        onClick = {
                            state.networkTypeNotRequired = true
                            state.networkTypeConnected = false
                            state.networkTypeUnmetered = false
                        }
                    )
                    Text(
                        text = stringResource(R.string.network_not_required),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = state.networkTypeConnected,
                        onClick = {
                            state.networkTypeNotRequired = false
                            state.networkTypeConnected = true
                            state.networkTypeUnmetered = false
                        }
                    )
                    Text(
                        text = stringResource(R.string.network_any_connection),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = state.networkTypeUnmetered,
                        onClick = {
                            state.networkTypeNotRequired = false
                            state.networkTypeConnected = false
                            state.networkTypeUnmetered = true
                        }
                    )
                    Text(
                        text = stringResource(R.string.network_wifi),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            // Other Constraints
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = state.requiresBatteryNotLow,
                    onCheckedChange = { state.requiresBatteryNotLow = it }
                )
                Text(
                    text = stringResource(R.string.battery_not_low),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = state.requiresCharging,
                    onCheckedChange = { state.requiresCharging = it }
                )
                Text(
                    text = stringResource(R.string.device_charging),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = state.requiresStorageNotLow,
                    onCheckedChange = { state.requiresStorageNotLow = it }
                )
                Text(
                    text = stringResource(R.string.storage_not_low),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Retry Policy
            Text(
                text = stringResource(R.string.retry_policy),
                style = MaterialTheme.typography.bodyMedium
            )
            
            Column(
                modifier = Modifier
                    .selectableGroup()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = state.useLinearBackoff,
                        onClick = {
                            state.useLinearBackoff = true
                            state.useExponentialBackoff = false
                        }
                    )
                    Text(
                        text = stringResource(R.string.linear_backoff),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = state.useExponentialBackoff,
                        onClick = {
                            state.useLinearBackoff = false
                            state.useExponentialBackoff = true
                        }
                    )
                    Text(
                        text = stringResource(R.string.exponential_backoff),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusCard(
    state: AdvancedConstraintsWorkState,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Title
            Text(
                text = stringResource(R.string.work_status),
                style = MaterialTheme.typography.titleMedium
            )
            
            // Status
            Text(
                text = state.workStatus,
                style = MaterialTheme.typography.bodyMedium
            )
            
            // Attempts
            Text(
                text = stringResource(R.string.attempts_count, state.attemptCount),
                style = MaterialTheme.typography.bodyMedium
            )
            
            // Result (if any)
            state.workResult?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
} 