package com.antonioleiva.frameworksamples.fragments.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.compose.content
import androidx.navigation.fragment.findNavController
import com.antonioleiva.frameworksamples.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

/**
 * Home Fragment - Starting point of navigation
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = content {
        HomeScreen(
            onProfileClick = {
                findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
            },
            onSettingsClick = {
                findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
            }
        )
    }
}

@Composable
private fun HomeScreen(
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.navigation_home_title),
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(onClick = onProfileClick) {
            Text(text = stringResource(id = R.string.navigation_go_to_profile))
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = onSettingsClick) {
            Text(text = stringResource(id = R.string.navigation_go_to_settings))
        }
    }
} 