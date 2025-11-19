package com.example.smitpatelsanjeevchauhan_comp304sec001_lab4.view

import android.annotation.SuppressLint
import android.location.Location
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.smitpatelsanjeevchauhan_comp304sec001_lab4.model.LocationPlace
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    selectedPlace: LocationPlace,
    userLocation: Location?,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    // Marker position that can change when user taps on the map
    var markerPosition by remember {
        mutableStateOf(selectedPlace.location)
    }

    var uiSettings by remember {
        mutableStateOf(MapUiSettings(zoomControlsEnabled = true))
    }

    // 1. State for the current map type
    var currentMapType by remember {
        mutableStateOf(MapType.SATELLITE) // Initial map type
    }

    // 2. MapProperties updated based on the currentMapType state
    val mapProperties by remember(currentMapType) { // Recompose when currentMapType changes
        mutableStateOf(MapProperties(mapType = currentMapType))
    }

    // Camera starts focused on the selected place
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(selectedPlace.location, 15f)
    }

    val userLatLng = userLocation?.let { LatLng(it.latitude, it.longitude) }

    // Distance text
    val distanceText = remember(userLocation?.latitude, userLocation?.longitude, selectedPlace) {
        if (userLocation == null) {
            "Distance: locating…"
        } else {
            val results = FloatArray(1)
            Location.distanceBetween(
                userLocation.latitude,
                userLocation.longitude,
                selectedPlace.location.latitude,
                selectedPlace.location.longitude,
                results
            )
            val meters = results[0]

            // Handle distances formatting
            if (meters < 1000f) {
                "Distance: ${meters.toInt()} m"
            } else {
                "Distance: %.2f km".format(meters / 1000f)
            }
        }
    }

    // Keep camera and marker focused on the selected place when it changes
    LaunchedEffect(selectedPlace) {
        markerPosition = selectedPlace.location
        cameraPositionState.position = CameraPosition.fromLatLngZoom(
            selectedPlace.location,
            15f
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = selectedPlace.name)
                        Text(
                            text = distanceText,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    compassEnabled = true,
                    myLocationButtonEnabled = true
                ),
                // handle taps on the map
                onMapClick = { latLng ->
                    // Move marker to this new position
                    markerPosition = latLng

                    // Show toast with lat / lng
                    Toast.makeText(
                        context,
                        "You clicked here: ${latLng.latitude}, ${latLng.longitude}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ) {
                // Destination marker (now uses markerPosition so it moves)
                Marker(
                    state = MarkerState(position = markerPosition),
                    title = selectedPlace.name,
                    snippet = selectedPlace.address
                )

                // User marker
                if (userLatLng != null) {
                    Marker(
                        state = MarkerState(position = userLatLng),
                        title = "You are here"
                    )
                }
            }

            // UI to change map type
            MapTypeControls(
                currentMapType = currentMapType,
                onMapTypeSelected = { newMapType ->
                    currentMapType = newMapType
                }
            )
        }
    }
}

@Composable
fun MapTypeControls(
    currentMapType: MapType,
    onMapTypeSelected: (MapType) -> Unit,
    modifier: Modifier = Modifier
) {
    val mapTypes = listOf(
        MapType.NORMAL,
        MapType.SATELLITE,
        MapType.TERRAIN,
        MapType.HYBRID,
        MapType.NONE // Useful for a completely custom base layer
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Select Map Type:")
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            mapTypes.forEach { mapType ->
                Button(
                    onClick = { onMapTypeSelected(mapType) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp),
                    enabled = mapType != currentMapType // Disable button for current type
                ) {
                    Text(mapType.toString())
                }
            }
        }
    }
}
