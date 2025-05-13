package com.example.mobilebanking.model

data class ElectricBill(
    var electricBillId: String = "",
    var billedUserName: String = "",
    var address: String = "",
    var phoneNumber: String = "",
    var billedAmount: Int = 0,
    var tax: Int = 0,
    var isPaid: Boolean = false,
    var dateStart: String = "",
    var dateEnd: String = ""
)