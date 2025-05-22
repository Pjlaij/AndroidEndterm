package com.example.mobilebanking.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

data class UtilityItem(val icon: String, val label: String)

@Composable
fun UtilitiesScreen(
    navController: NavController,
    onUtilityClick: (String) -> Unit = {} // sau này dùng để điều hướng
) {
    val utilities = listOf(
        UtilityItem("💡", "Tiền điện"),
        UtilityItem("🚿", "Tiền nước"),
        UtilityItem("📱", "Nạp điện thoại"),
        UtilityItem("✈️", "Vé máy bay"),
        UtilityItem("📺", "Truyền hình"),
        UtilityItem("📶", "Internet")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Tiện ích thanh toán", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(utilities) { utility ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clickable {
                            // Map label to screen route
                            val route = when (utility.label) {
                                "Tiền điện" -> "ElectricBillInputScreen"
                                "Tiền nước" -> "UtilitiesScreen"
                                "Nạp điện thoại" -> "UtilitiesScreen"
                                "Vé máy bay" -> "UtilitiesScreen"
                                "Truyền hình" -> "UtilitiesScreen"
                                "Internet" -> "InternetBillInputScreen"
                                else -> ""
                            }
                            if (route.isNotEmpty()) {
                                navController.navigate(route)
                            }
                        },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${utility.icon}\n${utility.label}",
                            fontSize = 18.sp,
                            lineHeight = 28.sp
                        )
                    }
                }
            }
        }
    }
}
@Composable
@Preview(showBackground = true)
fun UtilitiesScreenPreview() {
    val navController = rememberNavController()
    UtilitiesScreen(navController = navController)
}
//CHUA CO NUT BACK