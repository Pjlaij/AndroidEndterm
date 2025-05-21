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
import com.example.mobilebanking.viewmodel.UserInfoViewModel


@Composable
fun MortgageDetailScreen(navController: NavController) {
    val viewModel : UserInfoViewModel = viewModel()
    val context = LocalContext.current
    viewModel.loadUserInfo(context)
    val userInfo = viewModel.userInfo
    val mortgage = userInfo?.mortgage
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
            Text("Chi tiết tài khoản Mortgage", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (mortgage != null) {
            Text("Số dư hiện tại: {${mortgage.mortgage}}", fontSize = 18.sp)
            Text("Phải trả mỗi tháng: {${mortgage.monthlyPayment}}", fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { /* handle payment */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFAB91))
        ) {
            Text("Thanh toán", fontWeight = FontWeight.SemiBold)
        }
    }
}
