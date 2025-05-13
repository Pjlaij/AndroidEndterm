package com.example.mobilebanking.model

data class History (
    var from: String = "",
    var to: String = "",
    var amount: Int = 0,
    var type: String = "",
    var content: String = "",
    var timestamp: Long = 0L
)