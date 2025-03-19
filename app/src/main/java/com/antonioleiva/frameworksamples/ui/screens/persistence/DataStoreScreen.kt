package com.antonioleiva.frameworksamples.ui.screens.persistence

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.ui.components.Screen
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

private object PreferencesKeys {
    val TEXT = stringPreferencesKey("text")
}

@Serializable
object DataStoreScreen

@Composable
fun DataStoreScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var text by remember { mutableStateOf("") }

    LaunchedEffect(context) {
        val dataStoreFlow = context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.TEXT] ?: ""
        }

        dataStoreFlow.collect { savedText ->
            text = savedText
        }
    }

    Screen(
        title = stringResource(R.string.data_store),
        onBack = onBack
    ) { modifier ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(stringResource(R.string.text_hint)) },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    scope.launch {
                        context.dataStore.edit { preferences ->
                            preferences[PreferencesKeys.TEXT] = text
                        }
                    }
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(stringResource(R.string.save))
            }

            Button(
                onClick = {
                    scope.launch {
                        context.dataStore.edit { preferences ->
                            preferences.remove(PreferencesKeys.TEXT)
                        }
                    }
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(stringResource(R.string.delete))
            }

            Text(
                text = stringResource(R.string.data_store_instructions),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}