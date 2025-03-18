package com.antonioleiva.frameworksamples.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.antonioleiva.frameworksamples.ui.components.Screen

@Composable
fun HomeScreen(
    topics: List<String>,
    onTopicClick: (String) -> Unit
) {
    Screen(title = "Framework Samples") { modifier ->
        LazyColumn(
            modifier = modifier.fillMaxSize(),
        ) {
            items(topics) { topic ->
                TopicItem(
                    topic = topic,
                    onClick = { onTopicClick(topic) },
                )
            }
        }
    }
}

@Composable
fun TopicItem(
    topic: String,
    onClick: () -> Unit,
) {
    Text(
        text = topic,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
    )
}