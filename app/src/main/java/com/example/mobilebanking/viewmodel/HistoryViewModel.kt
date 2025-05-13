package com.example.mobilebanking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {
    private val _historyList = MutableStateFlow<List<History>>(emptyList())
    val historyList: StateFlow<List<History>> = _historyList

    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun fetchHistory() {
        val uid = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            database.getReference("users")
                .child(uid)
                .child("history")
                .get()
                .addOnSuccessListener { snapshot ->
                    val historyItems = mutableListOf<History>()
                    for (child in snapshot.children) {
                        val history = child.getValue(History::class.java)
                        if (history != null) {
                            historyItems.add(history)
                        }
                    }
                    // Sort by timestamp descending
                    _historyList.value = historyItems.sortedByDescending { it.timestamp }
                }
                .addOnFailureListener {
                    // Handle error (log, show error state, etc.)
                }
        }
    }
}