package com.example.mobilebanking.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

class InterBankTransferViewModel : ViewModel() {
    var payState by mutableStateOf<InterbanktransferResult>(InterbanktransferResult.Idle)
        private set
    /////////NETWORK CALL///////////////////////
    private var success = true
    fun pay(username_from: String, account_from: String, bank: String, account_to: String, amount: Int, transfer_message: String ) {
        viewModelScope.launch {
            delay(2000) // Simulated delay
            //to do, update to dabase: (minus amount of money from account_from), (add amount of money to account_to), ,
            //update to history with transfer_message
            if (success){
                payState = InterbanktransferResult.Success
            }
            else{
                payState = InterbanktransferResult.Error("Something wrong...edit here...")
            }
        }
    }
    ////////NETWORK CALL////////////////


}



sealed class InterbanktransferResult {
    object Idle : InterbanktransferResult()
    object Loading : InterbanktransferResult()
    object Success : InterbanktransferResult()
    data class Error(val message: String) : InterbanktransferResult()
}