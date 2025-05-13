package com.example.mobilebanking.model

data class UserInfo(
    var name: String = "",
    var username: String = "",
    var email: String = "",
    var password: String = "",
    var phone: String = "",
    var accountNumber: String = "",
    var balance: Int = 0,
    var electricBill: List<ElectricBill> = listOf(),
    var internetBill: List<InternetBill> = listOf(),
    var waterBill: List<WaterBill> = listOf(),
    var savingAccount: SavingAccount = SavingAccount(),
    var mortgage: Mortgage? = null,
    val history: Map<String, History> = mapOf()
)