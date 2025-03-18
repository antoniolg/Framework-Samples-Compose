package com.antonioleiva.frameworksamples.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.antonioleiva.frameworksamples.model.Sample
import com.antonioleiva.frameworksamples.model.SamplesInfo

@Composable
fun SamplesScreen(
    samplesInfo: SamplesInfo,
    onSampleClick: (Sample) -> Unit,
    onBack: () -> Unit
) {
    val title = stringResource(samplesInfo.title)
    Screen(title = title, onBack = onBack) { modifier ->
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            items(samplesInfo.samples) { sample ->
                SampleItem(
                    sample = sample,
                    onClick = { onSampleClick(sample) }
                )
            }
        }
    }
} 