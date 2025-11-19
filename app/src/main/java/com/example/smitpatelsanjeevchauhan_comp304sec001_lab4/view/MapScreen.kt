package com.example.smitpatelsanjeevchauhan_comp304sec001_lab4.view

import android.annotation.SuppressLint
import android.location.Location
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.MapType
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

    // 1. State for Marker Position
    var markerPosition by remember { mutableStateOf(selectedPlace.location) }

    // 2. State for Marker Text (Title & Snippet) so it can change dynamically
    var markerTitle by remember { mutableStateOf(selectedPlace.name) }
    var markerSnippet by remember { mutableStateOf(selectedPlace.address) }

    var currentMapType by remember { mutableStateOf(MapType.SATELLITE) }

    val mapProperties by remember(currentMapType) {
        mutableStateOf(MapProperties(mapType = currentMapType))
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(selectedPlace.location, 15f)
    }

    val userLatLng = userLocation?.let { LatLng(it.latitude, it.longitude) }

    val distanceText = remember(userLocation?.latitude, userLocation?.longitude, selectedPlace) {
        if (userLocation == null) {
            "Distance: locating…"
        } else {
            val results = FloatArray(1)
            Location.distanceBetween(
                userLocation.latitude, userLocation.longitude,
                selectedPlace.location.latitude, selectedPlace.location.longitude,
                results
            )
            val meters = results[0]
            if (meters < 1000f) "Distance: ${meters.toInt()} m" else "Distance: %.2f km".format(meters / 1000f)
        }
    }

    // Reset everything if selectedPlace changes
    LaunchedEffect(selectedPlace) {
        markerPosition = selectedPlace.location
        markerTitle = selectedPlace.name
        markerSnippet = selectedPlace.address
        cameraPositionState.position = CameraPosition.fromLatLngZoom(selectedPlace.location, 15f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = selectedPlace.name, style = MaterialTheme.typography.titleMedium)
                        Text(text = distanceText, style = MaterialTheme.typography.bodySmall)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                )
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
                onMapClick = { latLng ->
                    // 1. Move the marker
                    markerPosition = latLng

                    // 2. Update the text variables to reflect the NEW location
                    markerTitle = "Dropped Pin"
                    markerSnippet = "Lat: %.4f, Lng: %.4f".format(latLng.latitude, latLng.longitude)

                    // 3. Show Toast
                    Toast.makeText(
                        context,
                        "Lat: ${latLng.latitude}, Lng: ${latLng.longitude}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ) {
                // 4. Use the dynamic variables here instead of hardcoded selectedPlace.name
                Marker(
                    state = MarkerState(position = markerPosition),
                    title = markerTitle,
                    snippet = markerSnippet
                )

                if (userLatLng != null) {
                    Marker(
                        state = MarkerState(position = userLatLng),
                        title = "You are here"
                    )
                }

                // Geofence circle
                Circle(
                    center = selectedPlace.location,
                    radius = 200.0, // 200 meters
                    strokeColor = Color.Red, // Red border
                    fillColor = Color.Red,   // Transparent red fill
                    strokeWidth = 4f
                )
            }

            MapTypeControls(
                currentMapType = currentMapType,
                onMapTypeSelected = { currentMapType = it },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 8.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
        MapType.HYBRID
    )

    Card(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.95f)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Layer:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 8.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(mapTypes) { mapType ->
                    val isSelected = mapType == currentMapType

                    FilterChip(
                        selected = isSelected,
                        onClick = { onMapTypeSelected(mapType) },
                        label = {
                            Text(
                                text = mapType.toString().lowercase().replaceFirstChar { it.uppercase() }
                            )
                        },
                        leadingIcon = if (isSelected) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else null
                    )
                }
            }
        }
    }
}