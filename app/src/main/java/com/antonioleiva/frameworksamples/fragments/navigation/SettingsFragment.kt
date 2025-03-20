package com.antonioleiva.frameworksamples.fragments.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.compose.content
import com.antonioleiva.frameworksamples.R

/**
 * Settings Fragment - Shows a settings screen
 */
class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = content {
        SettingsScreen()
    }
}

@Composable
private fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.navigation_settings_title),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        // Mock settings
        SettingItem(title = "Setting 1", description = "Enable feature 1")

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        
        SettingItem(title = "Setting 2", description = "Enable feature 2", initialValue = true)
        
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        
        SettingItem(title = "Setting 3", description = "Enable feature 3")
    }
}

@Composable
private fun SettingItem(
    title: String,
    description: String,
    initialValue: Boolean = false
) {
    var isEnabled by remember { mutableStateOf(initialValue) }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Switch(
            checked = isEnabled,
            onCheckedChange = { isEnabled = it },
            modifier = Modifier.align(Alignment.End)
        )
    }
} 