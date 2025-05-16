package com.example.mobilebanking.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.*
import com.google.firebase.database.FirebaseDatabase


class ChangePasswordViewModel : ViewModel() {
    var editState by mutableStateOf<EditResult>(EditResult.Idle)
        private set

    private val auth = FirebaseAuth.getInstance()
    private val dbRef = FirebaseDatabase.getInstance().getReference("users")

    fun changePassword(newPassword: String, confPassword: String) {
        editState = EditResult.Loading

        if (newPassword != confPassword) {
            editState = EditResult.Error("Passwords do not match.")
            return
        }

        val currentUser = auth.currentUser
        val uid = currentUser?.uid

        if (uid == null) {
            editState = EditResult.Error("User not logged in.")
            return
        }

        viewModelScope.launch {
            // Update in Realtime Database
            dbRef.child(uid).child("password").setValue(newPassword)
                .addOnSuccessListener {
                    // Optionally update in FirebaseAuth as well
                    currentUser.updatePassword(newPassword)
                        .addOnSuccessListener {
                            editState = EditResult.Success
                        }
                        .addOnFailureListener { authError ->
                            editState =
                                EditResult.Error("Failed to update FirebaseAuth password: ${authError.message}")
                        }
                }
                .addOnFailureListener { dbError ->
                    editState = EditResult.Error("Database error: ${dbError.message}")
                }
        }
    }
}


sealed class EditResult {
    object Idle : EditResult()
    object Loading : EditResult()
    object Success : EditResult()
    data class Error(val message: String) : EditResult()
}