package com.example.mobilebanking.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilebanking.model.*
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class SameBankTransferViewModel : ViewModel() {
    var payState by mutableStateOf<PayResult>(PayResult.Idle)
        private set

    private val dbRef = FirebaseDatabase.getInstance().getReference("users")

    fun pay(
        context: Context,
        receiverAccountNumber: String,
        amount: Int,
        content: String,
        account: String,
        type: String // should be either "main" or "saving"
    ) {
        payState = PayResult.Loading

        val prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val senderUid = prefs.getString("uid", null)

        if (senderUid == null) {
            payState = PayResult.Error("Sender not logged in.")
            return
        }

        viewModelScope.launch {
            dbRef.child(senderUid).get().addOnSuccessListener { senderSnapshot ->
                val sender = senderSnapshot.getValue(UserInfo::class.java)

                if (sender == null) {
                    payState = PayResult.Error("Sender not found.")
                    return@addOnSuccessListener
                }

                val senderBalance = if (account == "Saving")
                    sender.savingAccount?.savingAccountBalance ?: -1
                else
                    sender.balance

                if (senderBalance < amount) {
                    payState = PayResult.Error("Insufficient funds in $type account.")
                    return@addOnSuccessListener
                }

                // Step 2: Find receiver
                dbRef.get().addOnSuccessListener { allUsersSnapshot ->
                    var receiverUid: String? = null
                    var receiver: UserInfo? = null

                    for (userSnapshot in allUsersSnapshot.children) {
                        val user = userSnapshot.getValue(UserInfo::class.java)
                        if (user?.accountNumber == receiverAccountNumber) {
                            receiverUid = userSnapshot.key
                            receiver = user
                            break
                        }
                    }

                    if (receiverUid == null || receiver == null) {
                        payState = PayResult.Error("Receiver not found.")
                        return@addOnSuccessListener
                    }

                    // Update balances
                    val updatedReceiverBalance = receiver.balance + amount
                    dbRef.child(receiverUid).child("balance").setValue(updatedReceiverBalance)

                    if (account == "Saving") {
                        val updatedSenderSaving = senderBalance - amount
                        dbRef.child(senderUid)
                            .child("savingAccount")
                            .child("savingAccountBalance")
                            .setValue(updatedSenderSaving)
                    } else {
                        val updatedSenderBalance = senderBalance - amount
                        dbRef.child(senderUid)
                            .child("balance")
                            .setValue(updatedSenderBalance)
                    }

                    // Add history
                    var historyEntry = History()
                    if (account == "Saving") {
                         historyEntry = History(
                            from = sender.accountNumber + "'s Saving",
                            to = receiverAccountNumber,
                            amount = amount,
                            type = type,
                            content = content,
                            timestamp = System.currentTimeMillis()
                        )
                    }
                    else{
                         historyEntry = History(
                            from = sender.accountNumber,
                            to = receiverAccountNumber,
                            amount = amount,
                            type = type,
                            content = content,
                            timestamp = System.currentTimeMillis()
                        )
                    }

                    dbRef.child(senderUid).child("history").push().setValue(historyEntry)
                    dbRef.child(receiverUid).child("history").push().setValue(historyEntry)

                    payState = PayResult.Success

                }.addOnFailureListener {
                    payState = PayResult.Error("Failed to find receiver: ${it.message}")
                }

            }.addOnFailureListener {
                payState = PayResult.Error("Failed to load sender info: ${it.message}")
            }
        }
    }

}

sealed class PayResult {
    object Idle : PayResult()
    object Loading : PayResult()
    object Success : PayResult()
    data class Error(val message: String) : PayResult()
}