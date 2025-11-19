package com.example.smitpatelsanjeevchauhan_comp304sec001_lab4.geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val event = GeofencingEvent.fromIntent(intent) ?: return
        if (event.hasError()) return
        
        if (event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            val placeId = event.triggeringGeofences?.firstOrNull()?.requestId ?: "Unknown place"
            Toast.makeText(
                context,
                "You entered the area of $placeId",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
