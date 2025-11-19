package com.example.smitpatelsanjeevchauhan_comp304sec001_lab4.view

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.core.app.ActivityCompat
import com.example.smitpatelsanjeevchauhan_comp304sec001_lab4.ui.theme.SmitPatelSanjeevChauhan_COMP304Sec001_Lab4Theme
import com.google.android.gms.location.*
import android.annotation.SuppressLint
import com.example.smitpatelsanjeevchauhan_comp304sec001_lab4.geofence.GeofencingHelper
import com.example.smitpatelsanjeevchauhan_comp304sec001_lab4.model.LocationData


class SmitActivity : ComponentActivity() {

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private lateinit var locationRequest: LocationRequest
    private var locationCallback: LocationCallback? = null

    // Ask for FINE + COARSE when needed
    private val requestLocationPermissions =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            // When user responds, start updates if granted
            if (hasLocationPermission()) {
                startLocationUpdates()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val placeId = intent.getStringExtra("placeId")
        val place = LocationData.getPlaceById(placeId ?: "")

        setContent {
            SmitPatelSanjeevChauhan_COMP304Sec001_Lab4Theme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    if (place != null) {

                        var userLocation by remember { mutableStateOf<Location?>(null) }

                        // Start geofence and location updates once
                        LaunchedEffect(Unit) {
                            // Geofence for selected place
                            GeofencingHelper(this@SmitActivity)
                                .addGeofenceForPlace(place)

                            // Request permission if we don't have it, else start updates
                            if (!hasLocationPermission()) {
                                requestLocationPermissions.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            } else {
                                startLocationUpdates { loc ->
                                    userLocation = loc
                                }
                            }
                        }

                        // Pass location into the map
                        MapScreen(
                            selectedPlace = place,
                            userLocation = userLocation,
                            onBack = {finish()}
                        )
                    }
                }
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        val fine = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    // Start listening to REAL-TIME location
    @SuppressLint("MissingPermission")   // already check permission
    private fun startLocationUpdates(onLocationChanged: ((Location) -> Unit)? = null) {

        // Extra safety: never call location APIs if we don't have permission
        if (!hasLocationPermission()) return

        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            3000L // every 3 seconds
        ).setMinUpdateDistanceMeters(5f).build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val loc = result.lastLocation ?: return
                onLocationChanged?.invoke(loc)
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback as LocationCallback,
            mainLooper
        )
    }


    override fun onStop() {
        super.onStop()
        // Stop updates when leaving screen
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }
    }
}
