package com.example.mobilebanking.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilebanking.viewmodel.AdminViewModel

data class Customer(
    val name: String,
    val email: String,
    val accountNumber: String = "",
    val interest: Double = 0.0
)


@Composable
fun AdminScreen() {
    val viewModel: AdminViewModel = viewModel()
    val customers by viewModel.customers.collectAsState()
    var newName by remember { mutableStateOf("") }
    var newEmail by remember { mutableStateOf("") }
    var interestRateText by remember { mutableStateOf("6.0") }
    var showDialog by remember { mutableStateOf(false) }
    var selectedAccountNumber by remember { mutableStateOf("") }
    var balanceInput by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()) // Make the whole screen scrollable
    ) {
        Text("Quản lý khách hàng", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Show only 3 customer cards in a scrollable list with fixed height
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp), // Adjust height for approx. 3 cards
            userScrollEnabled = true
        ) {
            items(customers) { customer ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        selectedAccountNumber = customer.accountNumber
                        showDialog = true
                    },
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("👤 ${customer.name}")
                    Text("📧 ${customer.email}")
                    Text("💳 Số tài khoản: ${customer.accountNumber}")
                    Text("💰 Lãi suất: ${customer.interest}%")
                }
            }
        }

        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                    balanceInput = ""
                },
                title = { Text("Nạp tiền vào tài khoản") },
                text = {
                    OutlinedTextField(
                        value = balanceInput,
                        onValueChange = { balanceInput = it },
                        label = { Text("Số tiền") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        val amount = balanceInput.toIntOrNull()
                        if (amount != null && amount > 0) {
                            viewModel.addMoneyToAccount(selectedAccountNumber, amount)
                        }
                        showDialog = false
                        balanceInput = ""
                    }) {
                        Text("Xác nhận")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialog = false
                        balanceInput = ""
                    }) {
                        Text("Hủy")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Tạo tài khoản khách mới", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = newName,
            onValueChange = { newName = it },
            label = { Text("Họ và tên") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = newEmail,
            onValueChange = { newEmail = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (newName.isNotBlank() && newEmail.isNotBlank()) {
                    viewModel.addCustomer(Customer(newName, newEmail))
                    newName = ""
                    newEmail = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tạo khách hàng")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Cập nhật lãi suất tiết kiệm", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = interestRateText,
            onValueChange = { interestRateText = it },
            label = { Text("Lãi suất (%)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val rate = interestRateText.toDoubleOrNull()
                if (rate != null && rate > 0) {
                    viewModel.updateInterestRateForAll(rate.toString())
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cập nhật lãi suất")
        }
    }
}

