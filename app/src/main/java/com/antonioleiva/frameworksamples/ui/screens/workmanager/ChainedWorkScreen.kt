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
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import kotlinx.serialization.Serializable

@Serializable
object ChainedWorkScreen

@Composable
fun ChainedWorkScreen(onBack: () -> Unit) {
    val workState = rememberChainedWorkState()
    val scrollState = rememberScrollState()

    Screen(
        title = stringResource(R.string.work_manager_chained),
        onBack = onBack
    ) { paddingModifier ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .then(paddingModifier)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.chained_work_instructions),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )

            ChainTypeCard(
                isSequential = workState.isSequential,
                onSequentialChanged = { workState.isSequential = it }
            )

            WorkStatusCard(
                workAStatus = workState.workAStatus,
                workBStatus = workState.workBStatus,
                workCStatus = workState.workCStatus,
                finalResult = workState.finalResult
            )

            Button(
                onClick = { workState.startChainedWork() },
                enabled = !workState.isWorkRunning,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.start_chain_button))
            }

            Button(
                onClick = { workState.cancelChainedWork() },
                enabled = workState.isWorkRunning,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.cancel_work))
            }
        }
    }
}

@Composable
private fun ChainTypeCard(
    isSequential: Boolean,
    onSequentialChanged: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.chain_type_title),
                style = MaterialTheme.typography.titleMedium
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectableGroup()
            ) {
                ChainTypeOption(
                    text = stringResource(R.string.sequential_chain),
                    selected = isSequential,
                    onClick = { onSequentialChanged(true) }
                )

                ChainTypeOption(
                    text = stringResource(R.string.parallel_sequential_chain),
                    selected = !isSequential,
                    onClick = { onSequentialChanged(false) }
                )
            }
        }
    }
}

@Composable
private fun ChainTypeOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun WorkStatusCard(
    workAStatus: String,
    workBStatus: String,
    workCStatus: String,
    finalResult: String?
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.work_status_title),
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = workAStatus,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = workBStatus,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = workCStatus,
                style = MaterialTheme.typography.bodyMedium
            )

            if (finalResult != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = finalResult,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
} 