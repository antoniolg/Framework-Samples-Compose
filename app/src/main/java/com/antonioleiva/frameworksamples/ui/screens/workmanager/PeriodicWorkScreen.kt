package com.antonioleiva.frameworksamples.ui.screens.workmanager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.ui.components.Screen
import kotlinx.serialization.Serializable

@Serializable
object PeriodicWorkScreen

@Composable
fun PeriodicWorkScreen(onBack: () -> Unit) {
    val periodicWorkState = rememberPeriodicWorkState()
    val workData by periodicWorkState.workData.collectAsState()
    
    Screen(
        title = stringResource(R.string.work_manager_periodic),
        onBack = onBack
    ) { paddingModifier ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .then(paddingModifier)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Explanation text
            Text(
                text = stringResource(R.string.periodic_work_instructions),
                style = MaterialTheme.typography.bodyMedium
            )
            
            // Configuration Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.periodic_work_config_title),
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    // Interval selection
                    Text(
                        text = stringResource(R.string.interval_repeat_label),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    Column(Modifier.selectableGroup()) {
                        IntervalRadioButton(
                            text = stringResource(R.string.interval_15min_label),
                            selected = periodicWorkState.selectedIntervalMinutes == 15,
                            onClick = { periodicWorkState.selectedIntervalMinutes = 15 }
                        )
                        
                        IntervalRadioButton(
                            text = stringResource(R.string.interval_30min_label),
                            selected = periodicWorkState.selectedIntervalMinutes == 30,
                            onClick = { periodicWorkState.selectedIntervalMinutes = 30 }
                        )
                        
                        IntervalRadioButton(
                            text = stringResource(R.string.interval_1hour_label),
                            selected = periodicWorkState.selectedIntervalMinutes == 60,
                            onClick = { periodicWorkState.selectedIntervalMinutes = 60 }
                        )
                    }
                    
                    // Policy selection
                    Text(
                        text = stringResource(R.string.existing_work_policy_label),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    Column(Modifier.selectableGroup()) {
                        IntervalRadioButton(
                            text = stringResource(R.string.policy_keep),
                            selected = periodicWorkState.selectedPolicyKeep,
                            onClick = { periodicWorkState.selectedPolicyKeep = true }
                        )
                        
                        IntervalRadioButton(
                            text = stringResource(R.string.policy_replace),
                            selected = !periodicWorkState.selectedPolicyKeep,
                            onClick = { periodicWorkState.selectedPolicyKeep = false }
                        )
                    }
                }
            }
            
            // Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.work_status),
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Text(
                        text = periodicWorkState.workStatus,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    Text(
                        text = stringResource(R.string.last_run, workData.lastRun),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    Text(
                        text = stringResource(R.string.run_count, workData.runCount),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Action buttons
            Button(
                onClick = { periodicWorkState.schedulePeriodicWork() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.schedule_periodic_work))
            }
            
            Button(
                onClick = { periodicWorkState.cancelPeriodicWork() },
                modifier = Modifier.fillMaxWidth(),
                enabled = periodicWorkState.isWorkRunning
            ) {
                Text(text = stringResource(R.string.cancel_work))
            }
        }
    }
}

@Composable
private fun IntervalRadioButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(56.dp)
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null // null because we're handling the click on the entire row
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
} 