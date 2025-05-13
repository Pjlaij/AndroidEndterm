package com.example.mobilebanking.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mobilebanking.model.*

class SharedViewModel : ViewModel() {
    var selectedHistory by mutableStateOf<History?>(null)

}