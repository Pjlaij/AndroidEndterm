package com.example.mobilebanking.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mobilebanking.viewmodel.FaceIdSettingViewModel
import java.io.InputStream


@Composable
fun FaceIdSettingScreen(
    navController: NavController,
    initialEnabled: Boolean = false,
    onBack: () -> Unit = {},
    onSave: (Boolean, Bitmap?) -> Unit = { _, _ -> }
) {
    val viewModel: FaceIdSettingViewModel = viewModel()
    val context = LocalContext.current
    var isEnabled by remember { mutableStateOf(initialEnabled) }
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            capturedBitmap = bitmap
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Top Bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text("Face ID Settings", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "Enable Face ID for quick login and secure transactions.",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Face ID is ${if (isEnabled) "Enabled" else "Disabled"}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Switch(
                checked = isEnabled,
                onCheckedChange = { isEnabled = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF3F51B5)
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isEnabled) {
            Text("Capture your face:", fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(12.dp))

            // Use your reusable Composable
            com.example.mobilebanking.helper.CaptureFaceScreen(viewModel)
        }


        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { capturedBitmap?.let { viewModel.saveFaceEmbedding(context,
                capturedBitmap!!,{})} },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
        ) {
            Text("Save", fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

