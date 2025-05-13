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
import com.example.mobilebanking.model.*
import com.example.mobilebanking.viewmodel.UserInfoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElectricBillInputScreen(
    navController: NavController,
    onCheckBill: (String) -> Unit = {}
) {
    var billCode by remember { mutableStateOf("") }
    val userinfo: UserInfo
    val viewModel: UserInfoViewModel = viewModel()
    val context = LocalContext.current
    viewModel.loadUserInfo(context)
    var bill: ElectricBill?
    if (viewModel.userInfo != null){
        userinfo = viewModel.userInfo!!
    }
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
                    .clickable { navController.popBackStack() }
                    .size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text("Pay the bill", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Input Card
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = billCode,
                    onValueChange = { billCode = it },
                    label = { Text("Type electric bill code") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Please enter the correct bill code to check information.",
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { bill = viewModel.checkElectricBill(billCode); if (bill != null) { navController.navigate("ElectricBillConfirmationScreen/${billCode}")} },
                    enabled = billCode.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (billCode.isNotEmpty()) Color(0xFF3F51B5) else Color(0xFFE0E0E0),
                        contentColor = Color.White
                    )
                ) {
                    Text("Check", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ElectricBillInputScreenPreview() {
    var selectedTab by remember { mutableStateOf(0) }
    var navController = rememberNavController()
    ElectricBillInputScreen(
        navController = navController
    )
}