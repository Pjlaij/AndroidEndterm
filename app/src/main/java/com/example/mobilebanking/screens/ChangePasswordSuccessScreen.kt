package com.example.mobilebanking.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobilebanking.R


@Composable
fun ChangePasswordSuccessScreen(
    navController: NavController,
    onOkClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Image(
            painter = painterResource(id = R.drawable.password_success), // ← bạn thêm ảnh minh họa vào drawable
            contentDescription = null,
            modifier = Modifier.size(220.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Change password successfully!", fontSize = 18.sp, color = Color(0xFF4B3CC0))
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "You have successfully change password.\nPlease use the new password when Sign in.",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate("login") },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),

            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B3CC0))
        ) {
            Text("Ok", fontSize = 16.sp)
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ChangePasswordSuccessScreenPreview() {
    var navController = rememberNavController()
    ChangePasswordSuccessScreen(
        navController = navController
    )
}