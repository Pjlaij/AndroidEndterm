package com.example.mobilebanking.model

import com.google.android.gms.maps.model.LatLng

data class Branch(
    val name: String,
    val address: String,
    val distance: String,
    val latitude: Double,
    val longitude: Double
) {
    val location: LatLng
        get() = LatLng(latitude, longitude)
}


