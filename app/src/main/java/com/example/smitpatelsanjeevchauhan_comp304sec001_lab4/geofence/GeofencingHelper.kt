package com.example.smitpatelsanjeevchauhan_comp304sec001_lab4.geofence

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.smitpatelsanjeevchauhan_comp304sec001_lab4.model.LocationPlace
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.*

class GeofencingHelper(private val context: Context) {

    private val geofencingClient: GeofencingClient =
        LocationServices.getGeofencingClient(context)

    private fun geofencePendingIntent(): PendingIntent {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        intent.action = "com.example.GEOFENCE_EVENT"

        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE

        return PendingIntent.getBroadcast(
            context,
            0,
            intent,
            flags
        )
    }

    @SuppressLint("MissingPermission")
    fun addGeofenceForPlace(place: LocationPlace) {
        val geofence = Geofence.Builder()
            .setRequestId(place.id)
            .setCircularRegion(
                place.location.latitude,
                place.location.longitude,
                200f
            )
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .build()

        val request = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        geofencingClient.addGeofences(request, geofencePendingIntent())
            .addOnSuccessListener {
                Toast.makeText(context, "Geofence set for ${place.name}", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e: Exception ->
                if (e is ApiException) {
                    Log.e("GEOFENCE", "Geofence failed: ${e.statusCode}")
                } else {
                    Log.e("GEOFENCE", "Unknown error: ${e.message}")
                }
            }

    }
}
