package com.example.smitpatelsanjeevchauhan_comp304sec001_lab4.view

import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.smitpatelsanjeevchauhan_comp304sec001_lab4.model.LocationPlace
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.google.maps.android.compose.rememberCameraPositionState

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    selectedPlace: LocationPlace,
    userLocation: Location?,
    onBack: () -> Unit
) {
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

    // Keep camera focused on the selected place
    LaunchedEffect(selectedPlace) {
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
                properties = MapProperties(
                    isMyLocationEnabled = userLatLng != null
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    compassEnabled = true,
                    myLocationButtonEnabled = true
                )
            ) {
                // Destination marker
                Marker(
                    state = MarkerState(position = selectedPlace.location),
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
        }
    }
}
