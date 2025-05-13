package com.example.mobilebanking.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobilebanking.viewmodel.InterBankTransferViewModel
import com.example.mobilebanking.viewmodel.InterbanktransferResult
import com.example.mobilebanking.viewmodel.LoginResult
import com.example.mobilebanking.viewmodel.UserInfoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterBankTransferScreen(
    navController:NavController,
    onConfirm: () -> Unit = {}
) {
    var selectedSource by remember { mutableStateOf("Checking") }
    val sourceOptions = listOf("Checking", "Saving")
    val viewModel: InterBankTransferViewModel = viewModel()
    val userinfoViewModel: UserInfoViewModel = viewModel()
    val context = LocalContext.current
    userinfoViewModel.loadUserInfo(context)
    var selectedBank by remember { mutableStateOf("") }
    val banks = listOf("Vietcombank", "VietinBank", "BIDV", "TPBank", "MB Bank", "ACB", "Sacombank")

    var recipientAccount by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val userInfo = userinfoViewModel.userInfo
    var payState = viewModel.payState
    if (userInfo == null) {
        // Show loading while waiting for data
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Top Bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text("Chuyển tiền liên ngân hàng", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Source Account
        Text("Tài khoản nguồn", fontSize = 14.sp, fontWeight = FontWeight.Medium)
        ExposedDropdownMenuBox(
            expanded = false,
            onExpandedChange = {}
        ) {
            var expanded by remember { mutableStateOf(false) }
            OutlinedTextField(
                value = selectedSource,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                label = { Text("Chọn tài khoản") },
                trailingIcon = {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.clickable { expanded = !expanded })
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                sourceOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedSource = option
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Recipient Bank
        Text("Ngân hàng người nhận", fontSize = 14.sp, fontWeight = FontWeight.Medium)
        ExposedDropdownMenuBox(
            expanded = false,
            onExpandedChange = {}
        ) {
            var bankExpanded by remember { mutableStateOf(false) }
            OutlinedTextField(
                value = selectedBank,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                label = { Text("Chọn ngân hàng") },
                trailingIcon = {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.clickable { bankExpanded = !bankExpanded })
                }
            )
            DropdownMenu(
                expanded = bankExpanded,
                onDismissRequest = { bankExpanded = false }
            ) {
                banks.forEach { bank ->
                    DropdownMenuItem(
                        text = { Text(bank) },
                        onClick = {
                            selectedBank = bank
                            bankExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Recipient Account
        OutlinedTextField(
            value = recipientAccount,
            onValueChange = { recipientAccount = it },
            label = { Text("Số tài khoản người nhận") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Amount
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Số tiền") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Content
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Nội dung chuyển khoản") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Confirm button
        Button(
            onClick = {
                var account_selected = ""
                if (selectedSource == "Checking"){
                    account_selected = userInfo.accountNumber
                }
                else{
                    account_selected = userInfo.savingAccount.savingAccountId
                }
                viewModel.pay(userInfo.username, account_selected, selectedBank, recipientAccount, amount.toInt(), content )
                      },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9575CD))
        ) {
            Text("Xác nhận chuyển tiền", fontWeight = FontWeight.Bold)
        }
        when (payState) {
            is InterbanktransferResult.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is InterbanktransferResult.Success -> {
                // Navigate or show success message
                LaunchedEffect(Unit) {
                    navController.navigate("")
                }
            }
            is InterbanktransferResult.Error -> {
                Text(
                    text = (payState as InterbanktransferResult.Error).message,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            else -> {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InterBankTransferScreenPreview() {
    val navController = rememberNavController()
    InterBankTransferScreen(navController = navController)
}