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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.antonioleiva.frameworksamples.model.Topic
import com.antonioleiva.frameworksamples.ui.components.Screen
import kotlinx.serialization.Serializable

@Serializable
object HomeScreen

@Composable
fun HomeScreen(
    topics: List<Topic>,
    onTopicClick: (Topic) -> Unit
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
    topic: Topic,
    onClick: () -> Unit,
) {
    Text(
        text = stringResource(topic.stringRes),
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(16.dp)
            .fillMaxWidth()
    )
}