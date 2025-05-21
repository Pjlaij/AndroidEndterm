package com.example.mobilebanking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilebanking.screens.Customer
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {

    private val dbRef = FirebaseDatabase.getInstance().getReference("users")

    private val _customers = MutableStateFlow<List<Customer>>(emptyList())
    val customers: StateFlow<List<Customer>> = _customers

    init {
        fetchCustomers()
    }

    private fun fetchCustomers() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val customerList = mutableListOf<Customer>()
                for (userSnapshot in snapshot.children) {
                    val name = userSnapshot.child("name").getValue(String::class.java)
                    val email = userSnapshot.child("email").getValue(String::class.java)
                    if (!name.isNullOrBlank() && !email.isNullOrBlank()) {
                        customerList.add(Customer(name, email))
                    }
                }
                _customers.value = customerList
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors if necessary
            }
        })
    }

    fun addCustomer(customer: Customer) {
        val newUserRef = dbRef.push()
        val userData = mapOf(
            "name" to customer.name,
            "email" to customer.email,
            "username" to customer.email,
            "password" to "123456", // default password
            "accountNumber" to "ACCT${(100000..999999).random()}",
            "balance" to 0,
            "phone" to "",
            "savingAccount" to mapOf(
                "interest" to "",
                "period" to "",
                "savingAccountBalance" to -1,
                "savingAccountId" to "",
                "savingDateEnd" to "",
                "savingDateStart" to ""
            )
        )
        newUserRef.setValue(userData)
    }
    fun updateInterestRateForAll(rate: Double) {
        val dbRef = FirebaseDatabase.getInstance().getReference("users")
        dbRef.get().addOnSuccessListener { snapshot ->
            snapshot.children.forEach { userSnapshot ->
                val uid = userSnapshot.key ?: return@forEach
                dbRef.child(uid).child("savingAccount").child("interest").setValue(rate)
            }
        }.addOnFailureListener {
            // handle error
        }
    }
}
