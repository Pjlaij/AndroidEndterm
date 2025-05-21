package com.example.mobilebanking.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobilebanking.R
import com.example.mobilebanking.viewmodel.LoginResult
import com.example.mobilebanking.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    onLoginClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val loginState = viewModel.loginState
    var isAdmin = viewModel.isAdmin
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(Color(0xFF4B3CC0), shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Sign in",
                color = Color.White,
                fontSize = 24.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Welcome text + Lock image
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome Back", fontSize = 22.sp, color = Color(0xFF4B3CC0))
            Text("Hello there, sign in to continue", color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))
            Image(
                painter = painterResource(R.drawable.lock_icon),
                contentDescription = "Lock Icon",
                modifier = Modifier.size(100.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(imageVector = icon, contentDescription = null)
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Forgot your password?",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { /* TODO */ }
            )

            // Nút đăng nhập
            Button(
                onClick = { viewModel.login(context, email, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B3CC0))

            ) {
                Text("Sign in", fontSize = 16.sp)
            }
            /////////////////////////////////////
            when (loginState) {
                is LoginResult.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is LoginResult.Success -> {
                    // Navigate or show success message
                    LaunchedEffect(Unit) {
                        val username = viewModel.username
                            if (isAdmin) {
                                navController.navigate("AdminScreen") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                navController.navigate("home/${username ?: "Guest"}") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }

                    }
                }
                is LoginResult.Error -> {
                    Text(
                        text = (loginState as LoginResult.Error).message,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                else -> {}
            }


            /////////////////////////////////////
            // Icon quét khuôn mặt
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(R.drawable.face_scan),
                    contentDescription = "Face Scan",
                    modifier = Modifier.size(64.dp)
                )
            }

            // Chuyển sang Sign Up
            Row(
                modifier = Modifier
                    .clickable { navController.navigate("register") }
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Don't have an account? ", color = Color.Gray)
                Text(
                    "Sign Up",
                    color = Color(0xFF4B3CC0),
                    modifier = Modifier.clickable { navController.navigate("register") }
                )
            }
        }
    }
}
