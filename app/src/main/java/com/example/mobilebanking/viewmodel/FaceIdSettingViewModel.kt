package com.example.mobilebanking.viewmodel

import android.content.Context
import android.graphics.*
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class FaceIdSettingViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val databaseRef = FirebaseDatabase.getInstance().reference

    fun saveFaceEmbedding(context: Context, bitmap: Bitmap, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val faceBitmap = cropFaceFromBitmap(context, bitmap)
                if (faceBitmap == null) {
                    Log.d("FACE ID REGISTER", "No face detected.")
                    onResult(false)
                    return@launch
                }

                val embedding = bitmapToEmbedding(faceBitmap)
                val userId = auth.currentUser?.uid
                if (userId == null) {
                    Log.d("FACE ID REGISTER", "User not logged in.")
                    onResult(false)
                    return@launch
                }

                databaseRef.child("users").child(userId).child("faceEmbedding").setValue(embedding).await()

                Log.d("FACE ID REGISTER", "WORKED")
                onResult(true)

            } catch (e: Exception) {
                Log.e("FACE ID REGISTER", "FAILED", e)
                onResult(false)
            }
        }
    }



    fun compareFaceToStored(
        context: Context,
        capturedBitmap: Bitmap,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userId = auth.currentUser?.uid ?: return@launch onResult(false)
                val snapshot = databaseRef.child("users").child(userId).child("faceEmbedding").get().await()
                val storedEmbedding = snapshot.getValue(String::class.java) ?: return@launch onResult(false)

                val capturedFace = cropFaceFromBitmap(context, capturedBitmap) ?: return@launch onResult(false)
                val currentEmbedding = bitmapToEmbedding(capturedFace)

                val isMatch = compareEmbeddings(currentEmbedding, storedEmbedding)
                onResult(isMatch)
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    private suspend fun cropFaceFromBitmap(context: Context, bitmap: Bitmap): Bitmap? {
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        val detector = FaceDetection.getClient()

        val faces = detector.process(inputImage).await()
        if (faces.isEmpty()) return null

        val face = faces[0].boundingBox
        return Bitmap.createBitmap(bitmap, face.left.coerceAtLeast(0), face.top.coerceAtLeast(0),
            face.width().coerceAtMost(bitmap.width - face.left),
            face.height().coerceAtMost(bitmap.height - face.top))
    }

    private fun bitmapToEmbedding(bitmap: Bitmap): String {
        val resized = Bitmap.createScaledBitmap(bitmap, 128, 128, true)
        val stream = ByteArrayOutputStream()
        resized.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
    }

    private fun compareEmbeddings(base64A: String, base64B: String): Boolean {
        val a = Base64.decode(base64A, Base64.DEFAULT)
        val b = Base64.decode(base64B, Base64.DEFAULT)
        if (a.size != b.size) return false

        var dot = 0.0
        var normA = 0.0
        var normB = 0.0
        for (i in a.indices) {
            dot += a[i] * b[i]
            normA += a[i] * a[i]
            normB += b[i] * b[i]
        }

        val similarity = dot / (Math.sqrt(normA) * Math.sqrt(normB))
        return similarity > 0.92 // tune threshold
    }
}
