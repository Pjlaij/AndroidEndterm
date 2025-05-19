package com.example.mobilebanking.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobilebanking.model.Branch
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    navController: NavController

) {
    val context = LocalContext.current

    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    LaunchedEffect(locationPermissionState.status) {
        if (locationPermissionState.status.isGranted) {
            userLocation = getUserLocation(fusedLocationClient)
        } else {
            locationPermissionState.launchPermissionRequest()
        }
    }

    if (!locationPermissionState.status.isGranted) {
        // Show a rationale or fallback UI if needed
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Location permission is required to show nearby branches.")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
                Text("Grant Permission")
            }
        }
        return
    }

    val branches = listOf(
        Branch("Chi nhánh Quận 1", "123 Lê Lợi, Q1", "Cách bạn: 0.5 km", 10.7769, 106.7009),
        Branch("Chi nhánh Quận 3", "456 Nguyễn Đình Chiểu, Q3", "Cách bạn: 1.2 km", 10.7791, 106.6866),
        Branch("Chi nhánh Gò Vấp", "789 Quang Trung, Gò Vấp", "Cách bạn: 3.8 km", 10.8326, 106.6659)
    )

    val cameraPositionState = rememberCameraPositionState {
        userLocation?.let {
            position = CameraPosition.fromLatLngZoom(it, 13f)
        }
    }

    var selectedBranch by remember { mutableStateOf<Pair<String, LatLng>?>(null) }
    var polylinePoints by remember { mutableStateOf<List<LatLng>>(emptyList()) }

    LaunchedEffect(selectedBranch, userLocation) {
        if (selectedBranch != null && userLocation != null && userLocation != LatLng(0.0, 0.0)) {
            val (_, destLatLng) = selectedBranch!!
            val url = getDirectionsUrl(userLocation!!, destLatLng)
            val points = fetchPolylinePoints(url)
            polylinePoints = points
        }
    }

    Column(Modifier.fillMaxSize()) {
        // Top Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text("Branch", fontSize = 20.sp, modifier = Modifier.padding(start = 8.dp))
        }

        // Google Map
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            cameraPositionState = cameraPositionState
        ) {
            userLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "Vị trí của bạn"
                )
            }

            branches.forEach { branch ->
                Marker(
                    state = MarkerState(position = branch.location),
                    title = branch.name,
                    onClick = {
                        selectedBranch = branch.name to branch.location
                        false
                    }
                )
            }

            if (polylinePoints.isNotEmpty()) {
                Polyline(
                    points = polylinePoints,
                    color = Color.Blue,
                    width = 10f
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Branch List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            items(branches) { branch ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            selectedBranch = branch.name to branch.location
                            cameraPositionState.move(
                                CameraUpdateFactory.newLatLngZoom(branch.location, 14f)
                            )
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(branch.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(branch.address, fontSize = 14.sp, color = Color.Gray)
                        Text(branch.distance, fontSize = 13.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
@OptIn(ExperimentalCoroutinesApi::class)
suspend fun getUserLocation(fusedLocationClient: FusedLocationProviderClient): LatLng {
    return suspendCancellableCoroutine { cont ->
        val locationRequest = com.google.android.gms.location.LocationRequest
            .create()
            .setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setNumUpdates(1)
            .setInterval(1000)

        val callback = object : com.google.android.gms.location.LocationCallback() {
            override fun onLocationResult(result: com.google.android.gms.location.LocationResult) {
                val location: Location? = result.lastLocation
                if (location != null) {
                    cont.resume(LatLng(location.latitude, location.longitude), null)
                } else {
                    cont.resume(LatLng(10.762622, 106.660172), null) // fallback
                }
            }

            override fun onLocationAvailability(p0: com.google.android.gms.location.LocationAvailability) {
                super.onLocationAvailability(p0)
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, callback, null)
    }
}



fun getDirectionsUrl(origin: LatLng, dest: LatLng): String {
    val strOrigin = "origin=${origin.latitude},${origin.longitude}"
    val strDest = "destination=${dest.latitude},${dest.longitude}"
    val key = "AIzaSyAFjLo5hZR_sRDklw7C1ZLiO60dE0lRzwc" // replace with your key
    return "https://maps.googleapis.com/maps/api/directions/json?$strOrigin&$strDest&key=$key"
}

suspend fun fetchPolylinePoints(url: String): List<LatLng> = withContext(Dispatchers.IO) {
    val result = URL(url).readText()
    val json = JSONObject(result)
    val routes = json.getJSONArray("routes")

    if (routes.length() == 0) {
        // Log error, return empty list
        return@withContext emptyList()
    }

    val steps = routes.getJSONObject(0)
        .getJSONObject("overview_polyline")
        .getString("points")

    decodePolyline(steps)
}


fun decodePolyline(encoded: String): List<LatLng> {
    val poly = mutableListOf<LatLng>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0

    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lat += dlat

        shift = 0
        result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lng += dlng

        val point = LatLng(lat / 1E5, lng / 1E5)
        poly.add(point)
    }

    return poly
}
