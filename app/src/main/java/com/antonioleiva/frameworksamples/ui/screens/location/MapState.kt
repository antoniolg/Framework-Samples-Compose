package com.antonioleiva.frameworksamples.ui.screens.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberMapState(
    context: Context = LocalContext.current,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    onPermissionDenied: () -> Unit = {}
) = remember(context, cameraPositionState) {
    MapState(
        context = context,
        cameraPositionState = cameraPositionState,
        onPermissionDenied = onPermissionDenied
    )
}

class MapState(
    val context: Context,
    val cameraPositionState: CameraPositionState,
    private val onPermissionDenied: () -> Unit
) {
    // Default location (Madrid)
    private val defaultLocation = LatLng(40.416775, -3.703790)
    
    // Current location
    var currentLocation by mutableStateOf<LatLng?>(null)
    
    // Current camera zoom level
    var zoomLevel by mutableStateOf(15f)
    
    // Collection of markers on the map
    val markers = mutableStateListOf<MarkerOptions>()
    
    // Marker counter for unique titles
    private var markerCount = 0
    
    // Fused location provider
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    
    // Check and request location permission
    fun checkLocationPermissionAndExecute(
        permissionLauncher: ActivityResultLauncher<Array<String>>,
        onPermissionGranted: () -> Unit
    ) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // We have permission, execute the action
                onPermissionGranted()
            }
            else -> {
                // Request permission
                requestLocationPermission(permissionLauncher)
            }
        }
    }
    
    // Get current location and move camera
    fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onPermissionDenied()
            return
        }
        
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                currentLocation = latLng
                
                // Move camera to current location
                cameraPositionState.move(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition.Builder()
                            .target(latLng)
                            .zoom(zoomLevel)
                            .build()
                    )
                )
            }
        }
    }
    
    // Add marker at specific location
    fun addMarker(latLng: LatLng) {
        markerCount++
        val markerOptions = MarkerOptions()
            .position(latLng)
            .title(context.getString(com.antonioleiva.frameworksamples.R.string.map_marker_title, markerCount))
            .snippet(
                context.getString(
                    com.antonioleiva.frameworksamples.R.string.map_marker_snippet,
                    latLng.latitude,
                    latLng.longitude
                )
            )
        
        markers.add(markerOptions)
    }
    
    // Clear all markers
    fun clearMarkers() {
        markers.clear()
        markerCount = 0
    }
    
    // Add marker at current location
    fun addMarkerAtCurrentLocation() {
        currentLocation?.let { addMarker(it) }
    }
    
    // Move camera to a specific location
    fun moveCamera(latLng: LatLng, zoom: Float = zoomLevel) {
        cameraPositionState.move(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder()
                    .target(latLng)
                    .zoom(zoom)
                    .build()
            )
        )
    }
    
    // Move camera to current location or default if null
    fun moveCameraToCurrentLocation() {
        val location = currentLocation ?: defaultLocation
        moveCamera(location)
    }
    
    // Update zoom level
    fun updateZoomLevel(zoom: Float) {
        zoomLevel = zoom
    }
}

// Helper function to request location permissions
private fun requestLocationPermission(permissionLauncher: ActivityResultLauncher<Array<String>>) {
    permissionLauncher.launch(
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
} 