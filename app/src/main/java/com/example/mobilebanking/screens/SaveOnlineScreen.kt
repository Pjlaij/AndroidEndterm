package com.example.mobilebanking.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobilebanking.R

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveOnlineScreen(
    onBack: () -> Unit = {},
    onVerify: (String, String, String) -> Unit = { _, _, _ -> }
) {
    var selectedTerm by remember { mutableStateOf("12 months") }
    var selectedRate by remember { mutableStateOf("5%") }
    var amount by remember { mutableStateOf("3000") }
    var showTermDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Top bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .clickable { onBack() }
                    .size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text("Add", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Illustration
        Image(
            painter = painterResource(id = R.drawable.saving_illustration), // Replace with actual image resource
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Info Card
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = "Account 1900 8988 5456",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Account") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    "Available balance: 10000$",
                    color = Color(0xFF3F51B5),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 6.dp, bottom = 12.dp)
                )

                // Fixed: Make time deposit dropdown fully clickable
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showTermDialog = true }
                ) {
                    OutlinedTextField(
                        value = selectedTerm,
                        onValueChange = {},
                        enabled = false,
                        label = { Text("Choose time deposit") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Text(
                    "Interest rate $selectedRate / $selectedTerm",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 6.dp, bottom = 12.dp)
                )

                // Amount
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount (At least \$1000)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { onVerify("1900 8988 5456", selectedTerm, amount) },
                    enabled = amount.toIntOrNull() ?: 0 >= 1000,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (amount.toIntOrNull() ?: 0 >= 1000) Color(0xFF2B1EFF) else Color(0xFFE0E0E0),
                        contentColor = Color.White
                    )
                ) {
                    Text("Verify", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // Dialog chọn kỳ hạn
    if (showTermDialog) {
        TimeDepositDialog(
            onDismiss = { showTermDialog = false },
            onSelect = { term, rate ->
                selectedTerm = term
                selectedRate = rate
                showTermDialog = false
            }
        )
    }
}

@Composable
fun TimeDepositDialog(
    onDismiss: () -> Unit,
    onSelect: (String, String) -> Unit
) {
    val options = listOf(
        "3 months" to "4%",
        "6 months" to "4.5%",
        "12 months" to "5%",
        "16 months" to "5.5%",
        "24 months" to "6%"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { Text("Choose time deposit") },
        text = {
            Column {
                options.forEach { (term, rate) ->
                    Text(
                        text = "$term (Interest rate $rate)",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(term, rate) }
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }
    )
}
