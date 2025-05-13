package com.example.mobilebanking.screens

import android.content.Context
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
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
import com.example.mobilebanking.screens.components.BillRow
import com.example.mobilebanking.model.*
import com.example.mobilebanking.viewmodel.LoginResult
import com.example.mobilebanking.viewmodel.PayResult
import com.example.mobilebanking.viewmodel.UserInfoViewModel
import com.example.mobilebanking.viewmodel.BalanceUpdateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElectricBillConfirmationScreen(
    navController: NavController,
    billCode: String?
) {
    val viewModel: UserInfoViewModel = viewModel()
    val payViewModel: BalanceUpdateViewModel = viewModel()
    val userInfo = viewModel.userInfo
    val context = LocalContext.current
    var selectedAccount by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val accounts = userInfo?.let { listOf(it.accountNumber) }
    var sent_otp: String = ""
    var payState = payViewModel.payState

    if (userInfo == null) {
        // Show loading while waiting for data
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    Log.d("ElectricBill", "Received billCode: $billCode")
    val bill = viewModel.checkElectricBill(billCode)

    if (bill == null) {
        // Show message if bill not found
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Bill not found.")
        }
        return
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
            Text("Electric bill", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Bill Detail Card
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("All the Bills", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                BillRow("Name", bill.billedUserName)
                BillRow("Address", bill.address)
                BillRow("Phone number", bill.phoneNumber)
                BillRow("Code", bill.electricBillId)
                BillRow("From", bill.dateStart)
                BillRow("To", bill.dateEnd)
                BillRow("Electric fee", bill.billedAmount.toString(), valueColor = Color(0xFF3F51B5))
                BillRow("Tax", bill.tax.toString(), valueColor = Color(0xFF3F51B5))
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                BillRow("TOTAL", ((bill.billedAmount + bill.tax)).toString(), valueColor = Color.Red, bold = true)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Account dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedAccount,
                onValueChange = {},
                label = { Text("Choose account/ card") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = Color(0xFF3F51B5)
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                if (accounts != null) {
                    accounts.forEach { account ->
                        DropdownMenuItem(
                            text = { Text(account) },
                            onClick = {
                                selectedAccount = account
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // OTP input
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = otp,
                onValueChange = { otp = it },
                label = { Text("OTP") },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    if (userInfo != null) {
                        sent_otp = generateOtp()
                        sendOtpSms(context = context,userInfo.phone, sent_otp)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Get OTP")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Pay button
        Button(
            onClick = {

                if (otp == sent_otp){
                    if (userInfo != null) {
                        payViewModel.pay(userInfo.username, (bill.billedAmount + bill.tax), "Electricity" )
                    }
                }

                      },
            enabled = otp.isNotEmpty() && selectedAccount.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Pay the bill", fontWeight = FontWeight.Bold)
        }
        when (payState) {
            is PayResult.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is PayResult.Success -> {
                // Navigate or show success message
                LaunchedEffect(Unit) {
                    navController.navigate("ElectricBillSuccessScreen")
                }
            }
            is PayResult.Error -> {
                Text(
                    text = (payState as LoginResult.Error).message,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            else -> {}
        }
    }
}

fun sendOtpSms(context: Context, phoneNumber: String, otp: String ) {
    try {
        val smsManager = SmsManager.getDefault()
        val message = "Your OTP is: $otp"
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        Toast.makeText(context, "OTP sent to $phoneNumber", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Failed to send OTP", Toast.LENGTH_SHORT).show()
    }
}

fun generateOtp(length: Int = 6): String {
    val digits = "0123456789"
    return (1..length)
        .map { digits.random() }
        .joinToString("")
}
@Preview
@Composable
fun ElectricBillConfScreenPreview() {
    var selectedTab by remember { mutableStateOf(0) }
    var navController = rememberNavController()
    ElectricBillConfirmationScreen(
        navController = navController,
        billCode = "123"
    )
}