package com.example.mobilebanking.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.example.mobilebanking.model.*
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

class LoginViewModel : ViewModel() {
    var loginState by mutableStateOf<LoginResult>(LoginResult.Idle)
        private set
    var username by mutableStateOf<String?>(null)
        private set

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val dbRef = FirebaseDatabase.getInstance().getReference("users")

    fun login(context: Context, email: String, password: String) {
        loginState = LoginResult.Loading

        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    val uid = authResult.user?.uid ?: return@addOnSuccessListener

                    // Fetch user info from Realtime Database
                    dbRef.orderByChild("username").equalTo(email).get()
                        .addOnSuccessListener { snapshot ->
                            val userInfo = snapshot.children.firstOrNull()?.getValue(UserInfo::class.java)
                            if (userInfo != null) {
                                // Save accountNumber to SharedPreferences
                                val prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                                prefs.edit().putString("uid", uid).apply()
                                username = userInfo.username
                                loginState = LoginResult.Success
                            } else {
                                loginState = LoginResult.Error("User not found in database.")
                            }
                        }
                        .addOnFailureListener { dbError ->
                            loginState = LoginResult.Error("Failed to fetch user info: ${dbError.message}")
                        }
                }
                .addOnFailureListener { error ->
                    loginState = LoginResult.Error("Login failed: ${error.message}")
                }
        }
    }

    fun resetState() {
        loginState = LoginResult.Idle
        username = null
    }
}

sealed class LoginResult {
    object Idle : LoginResult()
    object Loading : LoginResult()
    object Success : LoginResult()
    data class Error(val message: String) : LoginResult()
}
