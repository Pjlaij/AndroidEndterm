package com.example.mobilebanking.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun AccountScreen(
    navController: NavController

) {
    val viewModel: UserInfoViewModel = viewModel()
    val context = LocalContext.current
    viewModel.loadUserInfo(context)
    var balance = -1
    var savingBalance = -1
    var savingDateStart = "01/01/1975"
    var savingDateEnd: String = "01/01/2999"
    var period = "12 tháng"
    var interest = "6%"
    var mortgage = -1
    var monthlyPayment = -1
    if (viewModel.userInfo == null) {
        // Show loading while waiting for data
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    viewModel.userInfo?.let { user ->
        balance = user.balance
        savingBalance = user.savingAccount.savingAccountBalance
        savingDateStart = user.savingAccount.savingDateStart
        savingDateEnd = user.savingAccount.savingDateEnd
        period = user.savingAccount.period
        interest = user.savingAccount.interest.toString()
        mortgage = user.mortgage?.mortgage ?: 0
        monthlyPayment = user.mortgage?.monthlyPayment ?: 0
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Top bar with back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Tài khoản của bạn",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            // Checking account
            AccountCard(navController = navController, title = "Checking", icon = Icons.Default.Home) {
                LabelWithValue("Số dư", balance.toString(), Color(0xFF2E7D32))
            }

            // Saving account
            AccountCard(navController = navController, title = "Saving", icon = Icons.Default.Savings) {
                LabelWithValue("Số dư", savingBalance.toString(), Color(0xFF2E7D32))
                LabelWithValue("Từ", savingDateStart)
                LabelWithValue("Đến", savingDateEnd)
                LabelWithValue("Kỳ hạn", period)
                LabelWithValue("Lãi suất", interest, Color(0xFF1976D2))
            }

            // Mortgage account
            AccountCard(navController = navController, title = "Mortgage", icon = Icons.Default.Money) {
                LabelWithValue("Số dư", mortgage.toString(), Color(0xFFC62828))
                LabelWithValue("Phải trả mỗi tháng", monthlyPayment.toString(), Color(0xFFC62828))
            }
        }
    }
}

@Composable
fun AccountCard(navController: NavController, title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .clickable {  }
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF1976D2),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
fun LabelWithValue(label: String, value: String, valueColor: Color = Color.Black) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        Text(text = value, color = valueColor, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Preview(showBackground = true)
@Composable
fun AccountScreenPreview() {
    var selectedTab by remember { mutableStateOf(0) }
    var navController = rememberNavController()
    AccountScreen(
        navController = navController
    )}