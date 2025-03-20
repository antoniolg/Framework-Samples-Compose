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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.ui.components.Screen
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun MapScreen(
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    // State for map type dropdown
    var showMapTypeDropdown by remember { mutableStateOf(false) }
    
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
    val mapProperties = remember(mapState.mapType) {
        MapProperties(
            mapType = mapState.mapType,
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
            
            // Control buttons
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
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Button(
                    onClick = {
                        mapState.addRandomMarker()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(R.string.map_add_marker))
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Drawing options
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        if (!mapState.drawPolyline()) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = context.getString(R.string.map_error_polyline),
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(R.string.map_draw_polyline))
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Button(
                    onClick = {
                        if (!mapState.drawPolygon()) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = context.getString(R.string.map_error_polygon),
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(R.string.map_draw_polygon))
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        if (!mapState.drawCircle()) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = context.getString(R.string.map_error_circle),
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(R.string.map_draw_circle))
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Button(
                    onClick = {
                        mapState.clearAll()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(R.string.map_clear_markers))
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Map type dropdown
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { showMapTypeDropdown = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = stringResource(R.string.map_change_type))
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )
                    }
                }
                
                DropdownMenu(
                    expanded = showMapTypeDropdown,
                    onDismissRequest = { showMapTypeDropdown = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.map_type_normal)) },
                        onClick = {
                            mapState.changeMapType(MapType.NORMAL)
                            showMapTypeDropdown = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.map_type_satellite)) },
                        onClick = {
                            mapState.changeMapType(MapType.SATELLITE)
                            showMapTypeDropdown = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.map_type_terrain)) },
                        onClick = {
                            mapState.changeMapType(MapType.TERRAIN)
                            showMapTypeDropdown = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.map_type_hybrid)) },
                        onClick = {
                            mapState.changeMapType(MapType.HYBRID)
                            showMapTypeDropdown = false
                        }
                    )
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
                        ),
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                    )
                }
                
                // Custom markers from our list
                mapState.markers.forEachIndexed { index, position ->
                    Marker(
                        state = MarkerState(position = position),
                        title = context.getString(R.string.map_marker_title, index + 1),
                        snippet = context.getString(
                            R.string.map_marker_snippet,
                            position.latitude,
                            position.longitude
                        ),
                        icon = BitmapDescriptorFactory.defaultMarker(index * 30f % 360f)
                    )
                }
                
                // Draw polylines, polygons, and circles
                for (polylineOptions in mapState.polylines) {
                    Polyline(
                        points = polylineOptions.points,
                        color = Color(polylineOptions.color),
                        width = polylineOptions.width
                    )
                }
                
                for (polygonOptions in mapState.polygons) {
                    Polygon(
                        points = polygonOptions.points,
                        fillColor = Color(polygonOptions.fillColor),
                        strokeColor = Color(polygonOptions.strokeColor)
                    )
                }
                
                for (circleOptions in mapState.circles) {
                    circleOptions.center?.let { center ->
                        Circle(
                            center = center,
                            radius = circleOptions.radius,
                            fillColor = Color(circleOptions.fillColor),
                            strokeColor = Color(circleOptions.strokeColor)
                        )
                    }
                }
            }
        }
    }
} 