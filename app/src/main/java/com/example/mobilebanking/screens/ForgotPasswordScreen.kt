package com.example.mobilebanking.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobilebanking.viewmodel.ForgotPasswordViewModel
import com.example.mobilebanking.viewmodel.TextResult

@Composable
fun ForgotPasswordScreen(
    navController: NavController
) {
    var phoneNumber by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }

    val isPhoneValid = phoneNumber.trim().length >= 9
    val viewModel: ForgotPasswordViewModel = viewModel()
    val context = LocalContext.current
    val activity = context as Activity
    val textState = viewModel.textState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        // Top bar
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Text(
                text = "Forgot password",
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Card container
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F6F6)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Type your phone number",
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done
                    ),
                    placeholder = { Text(text = "(+84)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "We texted you a code to verify your phone number",
                    fontSize = 14.sp,
                    color = Color.Gray
                )


                Button(
                    onClick = {
                        activity.let {
                            viewModel.sendPhoneVerificationCode(phoneNumber.trim(), it)
                        }
                    },
                    enabled = isPhoneValid,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPhoneValid) Color(0xFF4B3CC0) else Color(0xFFECECEC),
                        contentColor = if (isPhoneValid) Color.White else Color.Gray
                    )
                ) {
                    Text("Send")
                }
                if (textState is TextResult.CodeSent) {
                    OutlinedTextField(
                        value = code,
                        onValueChange = { code = it },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        label = { Text("Verification Code") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            viewModel.verifyCode(code.trim())
                        },
                        enabled = code.length >= 6,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4B3CC0),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Verify Code")
                    }

                }
                when (textState) {
                    is TextResult.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                    is TextResult.Error -> {
                        Text(
                            text = (textState as TextResult.Error).message,
                            color = Color.Red,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                    else -> {}
                }

            }
        }
    }

    LaunchedEffect(textState) {
        if (textState is TextResult.Success) {
            navController.navigate("UpdatePasswordScreen")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    var navController = rememberNavController()
    ForgotPasswordScreen(
        navController = navController
    )
}
