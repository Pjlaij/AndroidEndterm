package com.example.mobilebanking.model

data class InternetBill(
    var company: String = "",
    var internetBillId: String = "",
    var billedUserName: String = "",
    var address: String = "",
    var phoneNumber: String = "",
    var billedAmount: Int = 0,
    var tax: Int = 0,
    var isPaid: Boolean = false,
    var dateStart: String = "",
    var dateEnd: String = ""
)