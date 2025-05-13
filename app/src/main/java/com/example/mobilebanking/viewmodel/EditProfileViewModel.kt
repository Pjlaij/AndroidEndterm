package com.example.mobilebanking.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class EditProfileViewModel : ViewModel(){
    var editState by mutableStateOf<EditResult>(EditResult.Idle)
        private set
    private val auth = FirebaseAuth.getInstance()
    private val dbRef = FirebaseDatabase.getInstance().getReference("users")

    fun editProfile(name: String, email: String, phone: String){
        editState = EditResult.Loading
        val currentUser = auth.currentUser
        val uid = currentUser?.uid

        if (uid == null) {
            editState = EditResult.Error("User not logged in.")
            return
        }
        viewModelScope.launch {
            // Update in Realtime Database
            dbRef.child(uid).child("name").setValue(name)
                .addOnSuccessListener {

                }
                .addOnFailureListener { dbError ->
                    editState = EditResult.Error("Database error: ${dbError.message}")
                }
            dbRef.child(uid).child("email").setValue(email)
                .addOnSuccessListener {

                }
                .addOnFailureListener { dbError ->
                    editState = EditResult.Error("Database error: ${dbError.message}")
                }
            dbRef.child(uid).child("phone").setValue(phone)
                .addOnSuccessListener {

                }
                .addOnFailureListener { dbError ->
                    editState = EditResult.Error("Database error: ${dbError.message}")
                }

        }
    }
}