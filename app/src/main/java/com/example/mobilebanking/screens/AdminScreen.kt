package com.example.mobilebanking.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class Customer(
    val name: String,
    val email: String
)
@Preview
@Composable
fun AdminScreen(
    customers: List<Customer> = listOf(
        Customer("Nguyễn Văn A", "a@gmail.com"),
        Customer("Trần Thị B", "b@yahoo.com"),
        Customer("Lê Văn C", "c@hotmail.com")
    ),
    onCreateCustomer: (Customer) -> Unit = {},
    onUpdateInterestRate: (Double) -> Unit = {}
) {
    var interestRateText by remember { mutableStateOf("6.0") }
    var newName by remember { mutableStateOf("") }
    var newEmail by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Quản lý khách hàng", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Hiển thị danh sách khách
        customers.forEach { customer ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("👤 ${customer.name}")
                    Text("📧 ${customer.email}")
                }
            }
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
                    onCreateCustomer(Customer(newName, newEmail))
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
                    onUpdateInterestRate(rate)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cập nhật lãi suất")
        }
    }
}
