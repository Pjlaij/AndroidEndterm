package com.example.mobilebanking.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobilebanking.R
import com.example.mobilebanking.viewmodel.UserInfoViewModel

@Composable
fun SettingScreen(
    selectedTab: Int = 2,
    onTabSelected: (Int) -> Unit,
    onEditProfile: () -> Unit = {},
    onChangePassword: () -> Unit = {},
    onFaceIdSettings: () -> Unit = {},
    onMapAndLocations: () -> Unit = {},
    navController: NavController,
    viewModel: UserInfoViewModel = viewModel(),


    ) {
    val context = LocalContext.current
    viewModel.loadUserInfo(context)
    var username: String? = viewModel.userInfo?.username
    if (username == null){
        username = "TEST"
    }
    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            CustomBottomNavigation(
                selectedTab = selectedTab,
                onTabSelected = onTabSelected,
                navController = navController
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Header background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color(0xFF3F51B5)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.profile_sample),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = username,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                if (username != null) {
                    SettingItem(
                        icon = Icons.Default.Edit,
                        title = "Edit Profile",
                        onClick = { navController.navigate("EditProfileScreen") },
                        navController = navController,
                        username = username
                    )
                }
                Divider(thickness = 0.8.dp, color = Color.LightGray)

                if (username != null) {
                    SettingItem(
                        icon = Icons.Default.Lock,
                        title = "Change Password",
                        onClick = { navController.navigate("ChangePasswordScreen") },
                        navController = navController,
                        username = username
                    )
                }
                Divider(thickness = 0.8.dp, color = Color.LightGray)

                if (username != null) {
                    SettingItem(
                        icon = Icons.Default.Face,
                        title = "Face ID Settings",
                        onClick = { navController.navigate("FaceIdSettingScreen") },
                        navController = navController,
                        username = username
                    )
                }
                Divider(thickness = 0.8.dp, color = Color.LightGray)

                if (username != null) {
                    SettingItem(
                        icon = Icons.Default.LocationOn,
                        title = "Map and Locations",
                        onClick = {navController.navigate("MapScreen")},
                        navController = navController,
                        username = username
                    )
                }
                Divider(thickness = 0.8.dp, color = Color.LightGray)
            }

            Spacer(modifier = Modifier.height(80.dp)) // space for nav bar
        }
    }
}

@Composable
fun SettingItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit,
    navController: NavController,
    username: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color(0xFF3F51B5),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
@Preview(showBackground = true)
@Composable
fun SettingScreenPreview() {
    var selectedTab by remember { mutableStateOf(0) }
    var navController = rememberNavController()
    SettingScreen(
        selectedTab = selectedTab,
        onTabSelected = { selectedTab = it },
        navController = navController
    )
}