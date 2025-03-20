package com.antonioleiva.frameworksamples.ui.screens.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.rememberCameraPositionState

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
    
    // Current location
    var currentLocation by mutableStateOf<LatLng?>(null)
    
    // Current camera zoom level
    var zoomLevel by mutableFloatStateOf(15f)
    
    // Collection of markers on the map
    val markers = mutableStateListOf<LatLng>()
    
    // Map type
    var mapType by mutableStateOf(MapType.NORMAL)
    
    // Shape properties
    var polylines = mutableStateListOf<PolylineOptions>()
    var polygons = mutableStateListOf<PolygonOptions>()
    var circles = mutableStateListOf<CircleOptions>()
    
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
                moveCamera(latLng)
            }
        }
    }
    
    // Add marker at specific location
    fun addMarker(latLng: LatLng) {
        markerCount++
        // Add the marker position to our list
        markers.add(latLng)
    }
    
    // Add a random marker near current view
    fun addRandomMarker() {
        // Get current camera position as center
        val position = cameraPositionState.position.target
        
        // Add some randomness to the position
        val lat = position.latitude + (Math.random() - 0.5) * 0.01
        val lng = position.longitude + (Math.random() - 0.5) * 0.01
        val markerPosition = LatLng(lat, lng)
        
        // Add marker to the map
        markerCount++
        markers.add(markerPosition)
    }
    
    // Clear all markers and shapes
    fun clearAll() {
        markers.clear()
        polylines.clear()
        polygons.clear()
        circles.clear()
        markerCount = 0
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

    // Change map type
    fun changeMapType(type: MapType) {
        mapType = type
    }
    
    // Draw a polyline using the current markers
    fun drawPolyline(): Boolean {
        if (markers.size < 2) {
            return false
        }
        
        val polylineOptions = PolylineOptions()
            .addAll(markers)
            .width(5f)
            .color(Color.RED)
            
        polylines.add(polylineOptions)
        return true
    }
    
    // Draw a polygon using the current markers
    fun drawPolygon(): Boolean {
        if (markers.size < 3) {
            return false
        }
        
        val polygonOptions = PolygonOptions()
            .addAll(markers)
            .strokeColor(Color.BLUE)
            .fillColor(Color.argb(70, 0, 0, 255))
            
        polygons.add(polygonOptions)
        return true
    }
    
    // Draw a circle around the last marker
    fun drawCircle(): Boolean {
        if (markers.isEmpty()) {
            return false
        }
        
        // Use the last marker as center
        val center = markers.last()
        
        val circleOptions = CircleOptions()
            .center(center)
            .radius(500.0) // 500 meters
            .strokeColor(Color.GREEN)
            .fillColor(Color.argb(70, 0, 255, 0))
            
        circles.add(circleOptions)
        return true
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