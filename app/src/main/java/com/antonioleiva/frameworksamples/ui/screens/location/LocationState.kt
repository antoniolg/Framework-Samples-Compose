package com.antonioleiva.frameworksamples.ui.screens.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import com.antonioleiva.frameworksamples.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

@Composable
fun rememberLocationState(
    context: Context = LocalContext.current,
    initialUpdatesStatus: String = stringResource(R.string.location_updates_stopped),
    initialNoLastKnown: String = stringResource(R.string.location_no_last_known)
) = remember(context) {
    LocationState(
        context = context,
        initialUpdatesStatus = initialUpdatesStatus,
        initialNoLastKnown = initialNoLastKnown
    )
}

class LocationState(
    val context: Context,
    initialUpdatesStatus: String,
    private val initialNoLastKnown: String
) {
    // Fused location provider client
    val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    
    // Location request configuration
    val locationRequest: LocationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        5000 // Update interval in milliseconds
    ).apply {
        setMinUpdateDistanceMeters(10f)
        setWaitForAccurateLocation(true)
    }.build()
    
    // State for last known location
    var lastLocation by mutableStateOf<Location?>(null)
    var lastLocationStatus by mutableStateOf<String?>(null)
    
    // State for location updates
    var currentLocation by mutableStateOf<Location?>(null)
    var receivingLocationUpdates by mutableStateOf(false)
    var updatesStatus by mutableStateOf(initialUpdatesStatus)
    
    // Location callback for updates
    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            for (location in result.locations) {
                currentLocation = location
            }
        }
    }
    
    // Lifecycle management
    fun handleLifecycleEvent(event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                if (receivingLocationUpdates) {
                    startLocationUpdates()
                }
            }
            Lifecycle.Event.ON_PAUSE -> {
                stopLocationUpdates()
            }
            else -> { /* ignore */ }
        }
    }
    
    // Permission handling
    fun checkLocationPermissionAndExecute(
        permissionLauncher: ActivityResultLauncher<Array<String>>,
        onPermissionGranted: () -> Unit
    ) {
        when {
            // Check if we have the permissions
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
                // No explanation needed, request the permission
                requestLocationPermission(permissionLauncher)
            }
        }
    }
    
    // Get last location
    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    lastLocation = location
                    lastLocationStatus = null
                } else {
                    lastLocationStatus = initialNoLastKnown
                }
            }
            .addOnFailureListener { exception ->
                lastLocationStatus = exception.localizedMessage
            }
    }
    
    // Start location updates
    @SuppressLint("MissingPermission")
    fun startLocationUpdates(updatesStartedText: String = "") {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
        
        receivingLocationUpdates = true
        updatesStatus = updatesStartedText.ifEmpty { updatesStatus }
    }
    
    // Stop location updates
    fun stopLocationUpdates(updatesStoppedText: String = "") {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        
        receivingLocationUpdates = false
        updatesStatus = updatesStoppedText.ifEmpty { updatesStatus }
        currentLocation = null
    }
    
    // Clean up resources
    fun cleanup() {
        if (receivingLocationUpdates) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
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