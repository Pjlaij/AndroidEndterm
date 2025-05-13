package com.example.mobilebanking.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mobilebanking.viewmodel.History
import com.example.mobilebanking.viewmodel.HistoryViewModel
import com.example.mobilebanking.viewmodel.SharedViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HistoryScreen(navController: NavController,
    onTransactionClick: (History) -> Unit = {}
) {
    val historyViewModel: HistoryViewModel = viewModel()
    val historyList by historyViewModel.historyList.collectAsState()
    val sharedViewModel: SharedViewModel = viewModel(LocalContext.current as ViewModelStoreOwner)
    var selectedTab by remember { mutableStateOf("Tất cả") }
    val tabs = listOf("Tất cả", "Chuyển khoảng", "Điện", "Nước", "Di động", "Internet", "Khác")

    // Trigger fetchHistory when screen is first composed
    LaunchedEffect(Unit) {
        historyViewModel.fetchHistory()
    }

    val filteredTransactions = if (selectedTab == "Tất cả") {
        historyList
    } else {
        historyList.filter { it.type == selectedTab }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Bar
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
                    .clickable { /* Handle back */ }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Lịch sử giao dịch",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Tab Filter
        ScrollableTabRow(
            selectedTabIndex = tabs.indexOf(selectedTab),
            edgePadding = 16.dp,
            containerColor = Color.Transparent,
            contentColor = Color(0xFF1976D2)
        ) {
            tabs.forEach { tab ->
                Tab(
                    selected = selectedTab == tab,
                    onClick = { selectedTab = tab },
                    text = {
                        Text(
                            tab,
                            color = if (selectedTab == tab) Color.White else Color(0xFF1976D2),
                            modifier = Modifier
                                .background(
                                    color = if (selectedTab == tab) Color(0xFF1976D2) else Color.LightGray.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                )
            }
        }

        // Transaction List
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(filteredTransactions) { transaction ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {sharedViewModel.selectedHistory = transaction; navController.navigate("TransactionDetailScreen") },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = formatTimestamp(transaction.timestamp),
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Text(transaction.content, fontWeight = FontWeight.SemiBold)
                                Text(
                                    text = transaction.type,
                                    color = if (transaction.from == transaction.to) Color(0xFF2E7D32) else Color(0xFFC62828),
                                    fontSize = 14.sp
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Số tiền", fontSize = 12.sp, color = Color.Gray)
                                Text(
                                    text = "${transaction.amount}đ",
                                    color = if (transaction.from == transaction.to) Color(0xFF2E7D32) else Color(0xFFC62828),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun formatTimestamp(timestamp: Long): String {
    return try {
        val date = java.util.Date(timestamp)
        val format = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
        format.format(date)
    } catch (e: Exception) {
        "Invalid date"
    }
}
