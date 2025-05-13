package com.example.mobilebanking.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mobilebanking.screens.components.DropdownMenuBox
import androidx.compose.foundation.clickable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@Composable
fun TransactionScreen(
    navController: NavController,
    accountOptions: List<String> = listOf("Checking", "Saving"),
    onSubmitTransaction: (type: String, account: String, amount: Double) -> Unit = { _, _, _ -> }
) {
    var selectedType by remember { mutableStateOf("Nạp tiền") }
    var selectedAccount by remember { mutableStateOf(accountOptions.first()) }
    var amountText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Giao dịch", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Chọn loại giao dịch
        Text("Chọn loại giao dịch:")
        Row {
            listOf("Nạp tiền", "Rút tiền").forEach { type ->
                Row(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable { selectedType = type },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedType == type,
                        onClick = { selectedType = type }
                    )
                    Text(type)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Chọn tài khoản nguồn
        DropdownMenuBox(
            selectedOption = selectedAccount,
            options = accountOptions,
            onOptionSelected = { selectedAccount = it },
            label = "Tài khoản"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Nhập số tiền
        OutlinedTextField(
            value = amountText,
            onValueChange = { amountText = it },
            label = { Text("Số tiền") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val amount = amountText.toDoubleOrNull() ?: 0.0
                if (amount > 0) {
                    onSubmitTransaction(selectedType, selectedAccount, amount)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Xác nhận")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun TransactionScreenPreview() {
    var navController = rememberNavController()
    TransactionScreen(
        navController = navController
    )
}