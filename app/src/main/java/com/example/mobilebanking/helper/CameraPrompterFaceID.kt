package com.example.mobilebanking.helper

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilebanking.viewmodel.FaceIdSettingViewModel


@Composable
fun CaptureFaceScreen(viewModel: FaceIdSettingViewModel = viewModel()) {
    val context = LocalContext.current

    val verifyLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            viewModel.compareFaceToStored(context, it) { isMatch ->
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        context,
                        if (isMatch) "Face matched!" else "Face not recognized",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    val registerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bmp ->
        bmp?.let {
            viewModel.saveFaceEmbedding(context, it) { success ->
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(context, if (success) "Face registered" else "Failed to register", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    Button(onClick = { verifyLauncher.launch() }) {
        Text("Verify Face")
    }

    Spacer(modifier = Modifier.height(12.dp))

    Button(onClick = { registerLauncher.launch() }) {
        Text("Register Face")
    }
}


