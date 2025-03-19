package com.antonioleiva.frameworksamples.ui.screens.persistence

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.ui.components.Screen
import kotlinx.serialization.Serializable

private const val PREFS_NAME = "sample_prefs"
private const val KEY_TEXT = "text"

@Serializable
object SharedPreferencesScreen

@Composable
fun SharedPreferencesScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }
    var text by remember { mutableStateOf(prefs.getString(KEY_TEXT, "") ?: "") }

    DisposableEffect(prefs) {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_TEXT) {
                text = prefs.getString(KEY_TEXT, "") ?: ""
            }
        }

        prefs.registerOnSharedPreferenceChangeListener(listener)

        onDispose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    Screen(
        title = stringResource(R.string.shared_preferences),
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
                onClick = { prefs.edit { putString(KEY_TEXT, text) } },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(stringResource(R.string.save))
            }

            Button(
                onClick = { prefs.edit { remove(KEY_TEXT) } },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(stringResource(R.string.delete))
            }

            Text(
                text = stringResource(R.string.shared_preferences_instructions),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}