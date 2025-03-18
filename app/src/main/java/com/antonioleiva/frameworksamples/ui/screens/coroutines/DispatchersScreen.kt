package com.antonioleiva.frameworksamples.ui.screens.coroutines

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.ui.components.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

@Serializable
object DispatchersScreen

@Composable
fun DispatchersScreen(onBack: () -> Unit) {
    var cpuResult by remember { mutableStateOf("") }
    var ioResult by remember { mutableStateOf("") }
    var isCpuLoading by remember { mutableStateOf(false) }
    var isIoLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Screen(
        title = stringResource(R.string.coroutines_title),
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
                    isCpuLoading = true
                    cpuResult = ""

                    scope.launch(Dispatchers.Default) {
                        val result = performCPUTask()
                        withContext(Dispatchers.Main) {
                            cpuResult = result
                            isCpuLoading = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.run_cpu_task))
            }

            if (isCpuLoading) {
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = cpuResult,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            } else if (cpuResult.isNotEmpty()) {
                Text(
                    text = cpuResult,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Button(
                onClick = {
                    isIoLoading = true
                    ioResult = ""

                    scope.launch(Dispatchers.IO) {
                        val result = performIOTask()
                        withContext(Dispatchers.Main) {
                            ioResult = result
                            isIoLoading = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.run_io_task))
            }

            if (isIoLoading) {
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = ioResult,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            } else if (ioResult.isNotEmpty()) {
                Text(
                    text = ioResult,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Text(
                text = stringResource(R.string.coroutines_instructions),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

private fun performCPUTask(): String {
    var result = 0
    for (i in 1..1_000_000) {
        result += i
    }
    return "CPU Task Result: $result"
}

private suspend fun performIOTask(): String {
    delay(2000)
    return "IO Task Result: Completed after 2 seconds"
} 