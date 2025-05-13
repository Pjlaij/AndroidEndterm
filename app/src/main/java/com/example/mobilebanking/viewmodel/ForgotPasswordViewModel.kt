package com.example.mobilebanking.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.mobilebanking.screens.ForgotPasswordScreen
import java.util.concurrent.TimeUnit

class ForgotPasswordViewModel : ViewModel() {

    var textState by mutableStateOf<TextResult>(TextResult.Idle)
        private set

    var verificationId: String? by mutableStateOf(null)
        private set

    private val firebaseAuth = FirebaseAuth.getInstance()

    fun sendPhoneVerificationCode(phoneNumber: String, activity: Activity) {
        textState = TextResult.Loading
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    textState = TextResult.Success
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    textState = TextResult.Error("Verification failed: ${e.localizedMessage}")
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    this@ForgotPasswordViewModel.verificationId = verificationId
                    textState = TextResult.CodeSent
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyCode(code: String) {
        val id = verificationId
        if (id != null) {
            val credential = PhoneAuthProvider.getCredential(id, code)
            signInWithPhoneAuthCredential(credential)
        } else {
            textState = TextResult.Error("Verification ID is null")
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    textState = TextResult.Success
                } else {
                    textState = TextResult.Error("Invalid verification code.")
                }
            }
    }
}

sealed class TextResult {
    object Idle : TextResult()
    object Loading : TextResult()
    object Success : TextResult()
    object CodeSent : TextResult()
    data class Error(val message: String) : TextResult()
}
