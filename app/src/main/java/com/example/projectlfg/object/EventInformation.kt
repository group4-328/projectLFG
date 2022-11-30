package com.example.projectlfg.`object`

import android.icu.util.Calendar
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseUser

data class EventInformation(
    var name: String,
    var latLng: LatLng,
    var address: String,
    val startTime: String,
    var endTime: String,
    var attendess: Long,
    var information: String,
    var creator: String,
    var id: String
    ): java.io.Serializable