package com.example.mobilebanking.screens

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mobilebanking.viewmodel.SharedViewModel


@Composable
fun TransactionDetailScreen(
    navController: NavController
) {
    val sharedViewModel: SharedViewModel = viewModel(LocalContext.current as ViewModelStoreOwner)
    val history = sharedViewModel.selectedHistory
    var transactionTitle = ""
    var from = ""
    var to = ""
    var amount = 0
    var content = ""
    var timestamp: Long = 0L
    var timestamp_string = ""
    if (history != null) {
        transactionTitle = history.type
        from = history.from
        to = history.to
        amount = history.amount
        content = history.content
        timestamp = history.timestamp
        timestamp_string = formatTimestamp(timestamp)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {navController.popBackStack()}) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = transactionTitle,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                InfoRow(label = "From", value = from)
                InfoRow(label = "To", value = to)
                InfoRow(label = "Type", value = transactionTitle)
                InfoRow(label = "Timestamp", value = timestamp_string)

                Spacer(modifier = Modifier.height(12.dp))

                InfoRow(label = "Content", value = content)


                Spacer(modifier = Modifier.height(12.dp))

                InfoRow(label = "Amount", value = amount.toString(), color = Color(0xFFC62828), bold = true)
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String, color: Color = Color.Black, bold: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text(
            value,
            color = color,
            fontSize = 14.sp,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal
        )
    }
}
