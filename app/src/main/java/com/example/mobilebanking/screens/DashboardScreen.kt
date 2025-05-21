package com.example.mobilebanking.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobilebanking.viewmodel.UserInfoViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DashboardScreen(
    userName: String = "User123",
    viewModel: UserInfoViewModel = viewModel(),
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    navController: NavController
) {

    var showBalance by remember { mutableStateOf(false) }
    val userInfo = viewModel.userInfo
    val context = LocalContext.current
    viewModel.loadUserInfo(context)
    var name = "DEFAULT USER"
    var balance = -1000000
    var accountNumber = ""

    if (userInfo == null){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
    else{
        name = userInfo.name
        balance = userInfo.balance
        accountNumber = userInfo.accountNumber
    }
    Scaffold(
        containerColor = Color(0xFFF5F5F5),
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
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            // Top App Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1976D2).copy(alpha = 0.8f))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("VCB Digibank", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Row {
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
                        }
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White)
                        }
                        IconButton(onClick = {navController.navigate("login") }) {
                            Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color.White)
                        }
                    }
                }
            }

            // User Info Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(50.dp), tint = Color.Gray)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(name.uppercase(), color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(accountNumber, color = Color.Gray, fontSize = 14.sp)
                                IconButton(
                                    onClick = { },
                                    modifier = Modifier.size(20.dp)
                                ) {
                                    Icon(Icons.Outlined.ContentCopy, contentDescription = null, tint = Color(0xFF1976D2), modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Số dư: ", color = Color.Black)
                        Text(
                            text = if (showBalance) balance.toString() else "********",
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = { showBalance = !showBalance },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = if (showBalance) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = Color(0xFF1976D2),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        TextButton(onClick = {navController.navigate("HistoryScreen") }) {
                            Text("Lịch sử giao dịch", color = Color(0xFF1976D2))
                        }
                        TextButton(onClick = { navController.navigate("AccountScreen")}) {
                            Text("Tài khoản & Thẻ", color = Color(0xFF1976D2))
                        }
                    }
                }
            }

            // Features Section
            Text("Chức năng ưa thích", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp))

            val features = listOf(
                Triple(Icons.Default.Send, "Chuyển cùng NH", "SameBankTransferScreen"),
                Triple(Icons.Default.ReceiptLong, "Chuyển khác NH", "InterBankTransferScreen"),
                Triple(Icons.Default.AccountBalanceWallet, "Nạp ví", "CheckingDetailScreen"),
                Triple(Icons.Default.PhoneAndroid, "Nạp điện thoại", "UtilitiesScreen"),
                Triple(Icons.Default.DataUsage, "Nạp 4G/5G", "UtilitiesScreen"),
                Triple(Icons.Default.Savings, "TK tiết kiệm", "SavingDetailScreen"),
                Triple(Icons.Default.FlashOn, "Tiền điện", "ElectricBillInputScreen"),
                Triple(Icons.Default.Water, "Tiền nước", "UtilitiesScreen")
            )

            features.chunked(4).forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    row.forEach { (icon, label, route) ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(80.dp)) {

                            Box(

                                modifier = Modifier
                                    .size(60.dp)
                                    .background(Color(0xFFE3F2FD), CircleShape)
                                    .clickable{
                                    navController.navigate(route)
                            },
                                contentAlignment = Alignment.Center

                            ) {
                                Icon(icon, contentDescription = label, modifier = Modifier.size(32.dp), tint = Color(0xFF1976D2))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(label, fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
            }

            // Shopping Section
            Text("Mua sắm liền tay", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp))

            val services = listOf(
                Icons.Default.LocalTaxi to "VNPay Taxi",
                Icons.Default.Flight to "Vé máy bay",
                Icons.Default.Movie to "Xem phim",
                Icons.Default.Hotel to "Khách sạn",
                Icons.Default.Train to "Vé tàu",
                Icons.Default.DirectionsBus to "Vé xe",
                Icons.Default.LocalFlorist to "Hoa",
                Icons.Default.ShoppingCart to "VNShop"
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(services) { (icon, label) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(80.dp)) {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.size(64.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Icon(icon, contentDescription = label, modifier = Modifier.size(32.dp), tint = Color(0xFF1976D2))
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(label, fontSize = 12.sp, textAlign = TextAlign.Center)
                    }
                }
            }

            // Quản lý tài chính cá nhân
            Text(
                text = "Quản lý tài chính cá nhân",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Chi tiêu theo tháng", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Dữ liệu mẫu
                    val expenses = listOf(0.4f, 0.8f, 0.6f, 0.7f, 0.5f)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        expenses.forEachIndexed { index, value ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .width(16.dp)
                                        .height((value * 100).dp)
                                        .background(Color(0xFF1976D2), RoundedCornerShape(4.dp))
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("T${index + 1}", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Chi tiêu so với tháng trước: 8%", color = Color(0xFF1976D2), fontSize = 14.sp)
                        TextButton(onClick = { /* chuyển sang màn chi tiết */ }) {
                            Text("Xem chi tiết", color = Color(0xFF1976D2), fontSize = 14.sp)
                        }
                    }
                }
            }


            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun CustomBottomNavigation(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    navController: NavController
) {
    val items = listOf("Trang Chủ", "QR", "Cài đặt")
    val icons = listOf(Icons.Default.Home, Icons.Default.QrCodeScanner, Icons.Default.Settings)

    Box(
        Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Color.White)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color(0xFF1976D2)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, label ->
                if (index == 1) {
                    // QR
                    Box(
                        modifier = Modifier
                            .offset(y = (-20).dp)
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable { onTabSelected(index) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icons[index],
                            contentDescription = label,
                            tint = Color(0xFF1976D2),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            onTabSelected(index)
                            if (index == 2){
                                navController.navigate("settings")
                            }
                            if (index == 0){
                                navController.navigate("backhome")
                            }
                        }
                    ) {
                        Icon(
                            imageVector = icons[index],
                            contentDescription = label,
                            tint = if (selectedTab == index) Color.White else Color.White.copy(alpha = 0.6f),
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            label,
                            color = if (selectedTab == index) Color.White else Color.White.copy(alpha = 0.6f),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    var selectedTab by remember { mutableStateOf(0) }
    var navController = rememberNavController()
    DashboardScreen(
        userName = "TRAN PHẠM HUU PHÚC",
        selectedTab = selectedTab,
        onTabSelected = { selectedTab = it },
        navController = navController
    )
}
