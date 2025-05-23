package com.example.mobilebanking.model

data class SavingAccount(
    var savingAccountId: String = "",
    var savingAccountBalance: Int = 0,
    var savingDateStart: String = "",
    var savingDateEnd: String = "",
    var interest: String = "",
    var period: String = ""      // E.g., "6 tháng", "12 tháng"
)