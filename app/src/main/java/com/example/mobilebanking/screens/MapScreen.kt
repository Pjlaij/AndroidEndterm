package com.example.mobilebanking.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobilebanking.R
import androidx.compose.foundation.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController

data class Branch(
    val name: String,
    val address: String,
    val distance: String
)


@Composable
fun MapScreen(
    navController: NavController,
    onBackClick: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }

    val branches = listOf(
        Branch("Chi nhánh Quận 1", "123 Lê Lợi, Q1, TP.HCM", "Cách bạn: 0.5 km"),
        Branch("Chi nhánh Quận 3", "456 Nguyễn Đình Chiểu, Q3", "Cách bạn: 1.2 km"),
        Branch("Chi nhánh Gò Vấp", "789 Quang Trung, Gò Vấp", "Cách bạn: 3.8 km")
    )

    Column(modifier = Modifier.fillMaxSize()) {

        // Top Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            IconButton(onClick = { onBackClick() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text("Branch", fontSize = 20.sp, modifier = Modifier.padding(start = 8.dp))
        }

        // Map placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(Color(0xFFEAEAEA)),
            contentAlignment = Alignment.Center
        ) {
            // Replace this with Google Map view in real app
            Image(
                painter = painterResource(id = R.drawable.map_placeholder), // dùng hình bản đồ tĩnh
                contentDescription = "Map",
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Tìm chi nhánh") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Branch list
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            items(branches.filter { it.name.contains(searchQuery, ignoreCase = true) }) { branch ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(branch.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(branch.address, fontSize = 14.sp, color = Color.Gray)
                        Text(branch.distance, fontSize = 13.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}
