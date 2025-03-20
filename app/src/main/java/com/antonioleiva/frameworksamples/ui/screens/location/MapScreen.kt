package com.antonioleiva.frameworksamples.ui.screens.location

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.ui.components.Screen
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun MapScreen(
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    // Initialize map state
    val cameraPositionState = rememberCameraPositionState()
    val mapState = rememberMapState(
        cameraPositionState = cameraPositionState,
        onPermissionDenied = {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.location_permission_denied),
                    duration = SnackbarDuration.Long
                )
            }
        }
    )
    
    // Map UI settings
    val uiSettings = remember {
        MapUiSettings(
            zoomControlsEnabled = true,
            myLocationButtonEnabled = true,
            compassEnabled = true,
            mapToolbarEnabled = true
        )
    }
    
    // Map properties
    val mapProperties = remember {
        MapProperties(
            mapType = MapType.NORMAL,
            isMyLocationEnabled = true
        )
    }
    
    // Permission launcher
    val locationPermissionRequest = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                // Permission granted, get location
                mapState.getCurrentLocation()
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
        title = stringResource(R.string.map_title),
        onBack = onBack,
        snackbarHostState = snackbarHostState
    ) { modifier ->
        Column(
            modifier = modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.map_instructions),
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Map buttons
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        mapState.checkLocationPermissionAndExecute(
                            permissionLauncher = locationPermissionRequest
                        ) {
                            mapState.getCurrentLocation()
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(R.string.map_my_location))
                }
                
                Spacer(modifier = Modifier.padding(8.dp))
                
                Button(
                    onClick = {
                        mapState.clearMarkers()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(R.string.map_clear_markers))
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Google Map
            GoogleMap(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = uiSettings,
                onMapClick = { latLng ->
                    mapState.addMarker(latLng)
                },
                onMapLongClick = { latLng ->
                    mapState.moveCamera(latLng)
                }
            ) {
                // Current location marker
                mapState.currentLocation?.let { currentLocation ->
                    Marker(
                        state = MarkerState(position = currentLocation),
                        title = stringResource(R.string.map_current_location),
                        snippet = context.getString(
                            R.string.map_marker_snippet,
                            currentLocation.latitude,
                            currentLocation.longitude
                        )
                    )
                }
                
                // Custom markers
                for (markerOptions in mapState.markers) {
                    Marker(
                        state = MarkerState(position = markerOptions.position),
                        title = markerOptions.title,
                        snippet = markerOptions.snippet
                    )
                }
            }
        }
    }
} 