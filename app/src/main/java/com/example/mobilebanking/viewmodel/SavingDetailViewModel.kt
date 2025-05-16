package com.example.mobilebanking.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilebanking.model.History
import com.example.mobilebanking.model.UserInfo
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class SavingOnlineViewModel : ViewModel() {
    var savingState by mutableStateOf<PayResult>(PayResult.Idle)
        private set

    private val dbRef = FirebaseDatabase.getInstance().getReference("users")

    fun depositToSaving(context: Context, amount: Int) {
        savingState = PayResult.Loading

        val prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val uid = prefs.getString("uid", null)

        if (uid == null) {
            savingState = PayResult.Error("User not logged in.")
            return
        }

        viewModelScope.launch {
            dbRef.child(uid).get().addOnSuccessListener { snapshot ->
                val user = snapshot.getValue(UserInfo::class.java)

                if (user == null || user.balance < amount) {
                    savingState = PayResult.Error("Insufficient checking balance.")
                    return@addOnSuccessListener
                }

                val newBalance = user.balance - amount
                val newSavingBalance = (user.savingAccount?.savingAccountBalance ?: 0) + amount

                val updates = mapOf(
                    "balance" to newBalance,
                    "savingAccount/savingAccountBalance" to newSavingBalance
                )

                dbRef.child(uid).updateChildren(updates).addOnSuccessListener {
                    // Log history
                    val historyEntry = History(
                        from = user.accountNumber,
                        to = user.accountNumber,
                        amount = amount,
                        type = "Gửi tiết kiệm",
                        content = "Deposit to saving account",
                        timestamp = System.currentTimeMillis()
                    )
                    dbRef.child(uid).child("history").push().setValue(historyEntry)

                    savingState = PayResult.Success
                }.addOnFailureListener {
                    savingState = PayResult.Error("Failed to update balances: ${it.message}")
                }

            }.addOnFailureListener {
                savingState = PayResult.Error("Failed to load user data: ${it.message}")
            }
        }
    }

    fun withdrawFromSaving(context: Context, amount: Int) {
        savingState = PayResult.Loading

        val prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val uid = prefs.getString("uid", null)

        if (uid == null) {
            savingState = PayResult.Error("User not logged in.")
            return
        }

        viewModelScope.launch {
            dbRef.child(uid).get().addOnSuccessListener { snapshot ->
                val user = snapshot.getValue(UserInfo::class.java)

                if (user == null || (user.savingAccount?.savingAccountBalance ?: 0) < amount) {
                    savingState = PayResult.Error("Insufficient saving balance.")
                    return@addOnSuccessListener
                }

                val newBalance = user.balance + amount
                val newSavingBalance = user.savingAccount.savingAccountBalance - amount

                val updates = mapOf(
                    "balance" to newBalance,
                    "savingAccount/savingAccountBalance" to newSavingBalance
                )

                dbRef.child(uid).updateChildren(updates).addOnSuccessListener {
                    // Log history
                    val historyEntry = History(
                        from = user.accountNumber,
                        to = user.accountNumber,
                        amount = amount,
                        type = "Rút tiết kiệm",
                        content = "Withdraw from saving account",
                        timestamp = System.currentTimeMillis()
                    )
                    dbRef.child(uid).child("history").push().setValue(historyEntry)

                    savingState = PayResult.Success
                }.addOnFailureListener {
                    savingState = PayResult.Error("Failed to update balances: ${it.message}")
                }

            }.addOnFailureListener {
                savingState = PayResult.Error("Failed to load user data: ${it.message}")
            }
        }
    }
}
