package com.example.mobilebanking.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

class BalanceUpdateViewModel : ViewModel() {
    var payState by mutableStateOf<PayResult>(PayResult.Idle)
        private set
    /////////NETWORK CALL///////////////////////
    private var success = true
    fun pay(username: String, amount: Int, content: String) {
        viewModelScope.launch {
            delay(2000) // Simulated delay
            //to do update new balance to database
            //update to history
            if (success){
                payState = PayResult.Success
            }
            else{
                payState = PayResult.Error("Something wrong...edit here...")
            }
        }
    }
    ////////NETWORK CALL////////////////


}

//data class History(
//    var from: String,
//    var to: String,
//    var amount: Int,
//    var type: String )

//sealed class PayResult {
//    object Idle : PayResult()
//    object Loading : PayResult()
//    object Success : PayResult()
//    data class Error(val message: String) : PayResult()
//}