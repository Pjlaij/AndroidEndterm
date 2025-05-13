package com.example.mobilebanking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import com.example.mobilebanking.model.*
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


    class RegisterViewModel : ViewModel() {
        var registerState by mutableStateOf<RegisterResult>(RegisterResult.Idle)
            private set

        private val auth = FirebaseAuth.getInstance()
        private val dbRef = FirebaseDatabase.getInstance().getReference("users")

        fun register(name: String, email: String, password: String, phone: String) {
            registerState = RegisterResult.Loading
            viewModelScope.launch {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Firebase Auth succeeded
                            val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                            val accountNumber = generateAccountNumber()
                            val userInfo = UserInfo(
                                name = name,
                                username = email,
                                email = email,
                                password = password,
                                phone = phone,
                                accountNumber = accountNumber,
                                balance = 0,
                                electricBill = listOf(),
                                internetBill = listOf(),
                                waterBill = listOf(),
                                savingAccount = SavingAccount(),
                                mortgage = null,
                                history = (mapOf ())
                            )

                            // Save additional user info to Realtime Database (optional)
                            dbRef.child(userId).setValue(userInfo)
                                .addOnSuccessListener {
                                    registerState = RegisterResult.Success
                                }
                                .addOnFailureListener { dbError ->
                                    registerState = RegisterResult.Error(dbError.message ?: "Failed to save user info")
                                }
                        } else {
                            registerState = RegisterResult.Error(task.exception?.message ?: "Authentication failed")
                        }
                    }
            }
        }

        private fun generateAccountNumber(): String {
            return "ACCT" + (100000..999999).random().toString()
        }
    }





sealed class RegisterResult {
    object Idle : RegisterResult()
    object Loading : RegisterResult()
    object Success : RegisterResult()
    data class Error(val message: String) : RegisterResult()
}