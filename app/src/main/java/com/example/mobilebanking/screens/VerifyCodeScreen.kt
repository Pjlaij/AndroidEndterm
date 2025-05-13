package com.example.mobilebanking.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun VerifyCodeScreen(
    phoneNumberMasked: String = "(+84) 0398829xxx",
    onBackClick: () -> Unit = {},
    onResendClick: () -> Unit = {},
    onChangePasswordClick: () -> Unit = {},
    onChangePhoneClick: () -> Unit = {}
) {
    var code by remember { mutableStateOf("") }
    val isCodeValid = code.trim().length >= 4

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        // Top bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onBackClick() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(text = "Forgot password", fontSize = 20.sp, modifier = Modifier.padding(start = 8.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

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
                Text(text = "Type a code", fontSize = 14.sp, color = Color.Gray)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = code,
                        onValueChange = { code = it },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        placeholder = { Text("Code") },
                        modifier = Modifier.weight(1f)
                    )

                    Button(
                        onClick = { onResendClick() },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B3CC0))
                    ) {
                        Text("Resend")
                    }
                }

                Text(
                    text = "We texted you a code to verify your phone number $phoneNumberMasked",
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Text(
                    text = "This code will expire in 10 minutes after this message. If you don’t get a message.",
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Button(
                    onClick = { onChangePasswordClick() },
                    enabled = isCodeValid,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCodeValid) Color(0xFF4B3CC0) else Color(0xFFECECEC),
                        contentColor = if (isCodeValid) Color.White else Color.Gray
                    )
                ) {
                    Text("Change password")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                text = "Change your phone number",
                color = Color(0xFF4B3CC0),
                modifier = Modifier.clickable { onChangePhoneClick() }
            )
        }
    }
}
