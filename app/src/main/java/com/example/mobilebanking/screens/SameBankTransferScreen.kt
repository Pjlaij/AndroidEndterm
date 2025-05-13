package com.example.mobilebanking.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import com.example.mobilebanking.viewmodel.LoginResult
import com.example.mobilebanking.viewmodel.PayResult
import com.example.mobilebanking.viewmodel.SameBankTransferViewModel
import com.example.mobilebanking.viewmodel.UserInfoViewModel


@Composable
fun SameBankTransferScreen(
    navController: NavController,
    onConfirm: () -> Unit = {}
) {
    val viewModel : SameBankTransferViewModel = viewModel()
    val userInfoViewModel: UserInfoViewModel = viewModel()
    val context = LocalContext.current
    userInfoViewModel.loadUserInfo(context)
    val userInfo = userInfoViewModel.userInfo
    val payState = viewModel.payState
    var fromAccount by remember { mutableStateOf("Checking") }
    var recipientAccount by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val accountOptions = listOf("Checking", "Saving")

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
            Text("Chuyển tiền nội bộ", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Source Account Dropdown
        Text("Tài khoản nguồn", fontSize = 14.sp, fontWeight = FontWeight.Medium)
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = fromAccount,
                onValueChange = {},
                label = { Text("Tài khoản nguồn") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                accountOptions.forEach { account ->
                    DropdownMenuItem(
                        text = { Text(account) },
                        onClick = {
                            fromAccount = account
                            expanded = false
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
                if (userInfo != null) {
                    viewModel.pay(context, recipientAccount, amount.toInt(), content, "Chuyển khoảng")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
        ) {
            Text("Xác nhận chuyển tiền", fontWeight = FontWeight.Bold, color = Color.White)
        }
        when (payState) {
            is PayResult.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is PayResult.Success -> {
                // Navigate or show success message
                LaunchedEffect(Unit) {
                    navController.navigate("TransferSuccessScreen/$recipientAccount?amount=$amount")
                }
            }
            is PayResult.Error -> {
                Text(
                    text = (payState as PayResult.Error).message,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            else -> {}
        }
    }
}
@Composable
@Preview(showBackground = true)
fun SameBankTransferScreenPreview() {
    val navController = rememberNavController()
    SameBankTransferScreen(navController = navController)
}