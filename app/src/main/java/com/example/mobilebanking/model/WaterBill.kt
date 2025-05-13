package com.example.mobilebanking.model

data class WaterBill (
    var waterBillId: String = "",
    var billedAmount: Int = 0,
    var status: Boolean = false, // Paid or not
    var date: String = ""         // Billing date
    )
