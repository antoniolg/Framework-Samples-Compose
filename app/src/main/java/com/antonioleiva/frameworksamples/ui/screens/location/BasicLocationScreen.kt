package com.antonioleiva.frameworksamples.ui.screens.location

import android.Manifest
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.ui.components.Screen
import kotlinx.coroutines.launch

@Composable
fun BasicLocationScreen(onBack: () -> Unit = {}) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Create location state
    val locationState = rememberLocationState()

    // Handle lifecycle events
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            locationState.handleLifecycleEvent(event)
        }
        
        lifecycleOwner.lifecycle.addObserver(observer)
        
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            locationState.cleanup()
        }
    }

    // Permission launcher
    val locationPermissionRequest = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                // Permission granted, get location
                locationState.getLastLocation()
            }
            else -> {
                // Permission denied
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.location_permission_denied),
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }
    }

    // UI
    Screen(
        title = stringResource(R.string.location_basic_title),
        onBack = onBack,
        snackbarHostState = snackbarHostState
    ) { modifier ->
        Column(
            modifier = modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.location_basic_description),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Last known location card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.location_last_known),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    locationState.lastLocationStatus?.let { Text(text = it) }
                    locationState.lastLocation?.let { LocationDetails(location = it) }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            locationState.checkLocationPermissionAndExecute(
                                permissionLauncher = locationPermissionRequest
                            ) {
                                locationState.getLastLocation()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.location_get_last))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Location updates card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.location_updates),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = locationState.updatesStatus)

                    locationState.currentLocation?.let { LocationDetails(it) }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            if (locationState.receivingLocationUpdates) {
                                locationState.stopLocationUpdates(context.getString(R.string.location_updates_stopped))
                            } else {
                                locationState.checkLocationPermissionAndExecute(
                                    permissionLauncher = locationPermissionRequest
                                ) {
                                    locationState.startLocationUpdates(context.getString(R.string.location_updates_started))
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (locationState.receivingLocationUpdates) 
                                stringResource(R.string.location_stop_updates)
                            else 
                                stringResource(R.string.location_start_updates)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LocationDetails(location: Location) {
    Column {
        Text(
            text = stringResource(
                R.string.location_latitude,
                location.latitude
            )
        )
        Text(
            text = stringResource(
                R.string.location_longitude,
                location.longitude
            )
        )
        Text(
            text = stringResource(
                R.string.location_accuracy,
                location.accuracy
            )
        )
    }
}