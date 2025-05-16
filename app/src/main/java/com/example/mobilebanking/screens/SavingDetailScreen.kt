package com.example.mobilebanking.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.mobilebanking.viewmodel.UserInfoViewModel

@Composable
fun SavingDetailScreen(navController: NavController) {
    val viewModel: UserInfoViewModel = viewModel()
    val context = LocalContext.current
    val balance = viewModel.userInfo?.savingAccount?.savingAccountBalance ?: 0
    val interestStr = viewModel.userInfo?.savingAccount?.interest?.replace("%", "") ?: "0"
    val interest = interestStr.toIntOrNull() ?: 0
    val monthlyProfit = balance * interest / 100
    viewModel.loadUserInfo(context)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .padding(end = 12.dp)
            )
            Text("Chi tiết tài khoản Saving", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Số dư hiện tại: " + balance + "đ", fontSize = 18.sp)
        Text("Lãi suất: "+ interest + "%/năm", fontSize = 16.sp)
        Text("Lãi mỗi tháng: " + monthlyProfit + "đ", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(40.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(
                onClick = { navController.navigate("TransactionScreen")},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81D4FA))
            ) {
                Text("Nạp tiền")
            }
            Button(
                onClick = { navController.navigate("TransactionScreen") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFCDD2))
            ) {
                Text("Rút tiền")
            }
        }
    }
}
@Composable
@Preview(showBackground = true)
fun SavingDetailsScreenPreview() {
    val navController = rememberNavController()
    SavingDetailScreen(navController = navController)
}