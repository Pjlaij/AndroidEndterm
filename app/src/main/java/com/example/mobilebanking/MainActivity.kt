package com.example.mobilebanking

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mobilebanking.ui.theme.MobileBankingTheme
import com.example.mobilebanking.screens.LoginScreen
import com.example.mobilebanking.screens.RegisterScreen
import com.example.mobilebanking.screens.DashboardScreen
import com.example.mobilebanking.screens.AccountScreen
import com.example.mobilebanking.screens.TransactionScreen
import com.example.mobilebanking.screens.HistoryScreen
import com.example.mobilebanking.screens.UtilitiesScreen
import com.example.mobilebanking.screens.MapScreen
import com.example.mobilebanking.screens.AdminScreen
import com.example.mobilebanking.screens.ForgotPasswordScreen
import com.example.mobilebanking.screens.VerifyCodeScreen
import com.example.mobilebanking.screens.ChangePasswordScreen
import com.example.mobilebanking.screens.ChangePasswordSuccessScreen
import com.example.mobilebanking.screens.TransactionDetailScreen
import com.example.mobilebanking.screens.CheckingDetailScreen
import com.example.mobilebanking.screens.SavingDetailScreen
import com.example.mobilebanking.screens.MortgageDetailScreen
import com.example.mobilebanking.screens.SameBankTransferScreen
import com.example.mobilebanking.screens.InterBankTransferScreen
import com.example.mobilebanking.screens.OtpVerificationScreen
import com.example.mobilebanking.screens.TransferSuccessScreen
import com.example.mobilebanking.screens.InternetBillInputScreen
import com.example.mobilebanking.screens.InternetBillConfirmationScreen
import com.example.mobilebanking.screens.InternetBillSuccessScreen
import com.example.mobilebanking.screens.ElectricBillInputScreen
import com.example.mobilebanking.screens.ElectricBillConfirmationScreen
import com.example.mobilebanking.screens.ElectricBillSuccessScreen
import com.example.mobilebanking.screens.SaveOnlineScreen
import com.example.mobilebanking.screens.SaveOnlineSuccessScreen
import com.example.mobilebanking.screens.SettingScreen
import com.example.mobilebanking.screens.QRScreen
import com.example.mobilebanking.screens.EditProfileScreen
import com.example.mobilebanking.screens.UpdatePasswordScreen
import com.example.mobilebanking.screens.FaceIdSettingScreen
import android.Manifest
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mobilebanking.screens.ChangePasswordSuccessScreenPreview
import com.example.mobilebanking.viewmodel.LoginViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            if (ContextCompat.checkSelfPermission(LocalContext.current, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                    LocalContext.current as Activity,
                    arrayOf(Manifest.permission.SEND_SMS),
                    1
                )
            }
            var selectedTab by remember { mutableStateOf(0) }
            val navController = rememberNavController()
            val loginviewModel: LoginViewModel = viewModel()
            MobileBankingTheme {
                NavHost(navController = navController, startDestination = "login"){
                    composable("login"){
                        LoginScreen(navController)
                    }
                    composable("register"){
                        RegisterScreen(navController, onSignUpClick = {
                            navController.navigate("login")
                        })
                    }
                    composable("home/{username}"){ backStackEntry ->
                        val username = backStackEntry.arguments?.getString("username") ?: "Guest"
                        DashboardScreen(userName = username, selectedTab = 0, onTabSelected = { selectedTab = it }, navController = navController)
                    }
                    composable("AdminScreen"){ AdminScreen() }
                    composable("backhome"){ DashboardScreen(userName = "test", selectedTab = 0, onTabSelected = { selectedTab = it }, navController = navController) }
                    composable("settings"){
                        SettingScreen(2, onTabSelected = {selectedTab = it}, navController = navController)
                    }

                    composable("SameBankTransferScreen") { SameBankTransferScreen(navController) }
                    composable("InterBankTransferScreen") { InterBankTransferScreen(navController) }
                    composable("SavingDetailScreen") { SavingDetailScreen(navController) }
                    composable("UtilitiesScreen") { UtilitiesScreen(navController) }
                    composable("EditProfileScreen") {EditProfileScreen(navController)}
                    composable("AccountScreen"){ AccountScreen(navController) }
                    composable("MapScreen"){MapScreen(navController)}
                    composable("FaceIdSettingScreen"){ FaceIdSettingScreen(navController) }
                    composable("ChangePasswordScreen"){ ChangePasswordScreen(navController) }
                    composable("CheckingDetailScreen"){ CheckingDetailScreen(navController) }
                    composable("ElectricBillInputScreen"){ ElectricBillInputScreen(navController) }
                    composable("InternetBillInputScreen"){ InternetBillInputScreen(navController) }
                    composable(
                        route = "ElectricBillConfirmationScreen/{billCode}",
                        arguments = listOf(navArgument("billCode") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val billCode = backStackEntry.arguments?.getString("billCode")
                        ElectricBillConfirmationScreen(navController, billCode)
                    }
                    composable(
                        route = "InternetBillConfirmationScreen/{billCode}/{Company}",
                        arguments = listOf(
                            navArgument("billCode") { type = NavType.StringType },
                            navArgument("Company") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val billCode = backStackEntry.arguments?.getString("billCode") ?: ""
                        val Company = backStackEntry.arguments?.getString("Company") ?: ""
                        InternetBillConfirmationScreen(navController, billCode, Company)
                    }
                    composable("ElectricBillSuccessScreen"){ ElectricBillSuccessScreen(navController) }
                    composable("UpdatePasswordScreen"){ UpdatePasswordScreen(navController) }
                    composable("ChangePasswordSuccessScreen"){ ChangePasswordSuccessScreen(navController) }
                    composable("HistoryScreen"){ HistoryScreen(navController) }
                    composable("TransferSuccessScreen/{recipient}"){ backStackEntry ->

                                val recipient = backStackEntry.arguments?.getString("recipient") ?: ""
                                val amount = backStackEntry.arguments?.getString("amount") ?: ""
                                TransferSuccessScreen(recipient, amount, navController)
                    }
                    composable("TransactionScreen"){ TransactionScreen(navController) }
                    composable("TransactionDetailScreen"){ TransactionDetailScreen(navController) }
                    composable("MortgageDetailScreen"){ MortgageDetailScreen(navController) }

                }

//                LoginScreen()
//                RegisterScreen()
//                ForgotPasswordScreen()
//                VerifyCodeScreen()
//                ChangePasswordScreen()
//                ChangePasswordSuccessScreen()
//                MapScreen()
//                DashboardScreen()
//                HistoryScreen(onTransactionClick = {})
//                TransactionDetailScreen()
//                AccountScreen()
//                CheckingDetailScreen()
//                SavingDetailScreen()
//                MortgageDetailScreen()
//                SameBankTransferScreen()
//                InterBankTransferScreen()
//                OtpVerificationScreen(sourceAccount = "Checking", destinationBank = "VCB", destinationAccount = "0123456789", amount = "1.000.000đ", content = "Chuyển học phí")
//                TransferSuccessScreen(recipientName = "Amanda", amount = "$1,000", onConfirm = { /* Navigate to home or finish */ })
//
//                InternetBillInputScreen(
//                    onCheckClick = { company, code ->
//                        println("Company: $company, BillCode: $code")
//                    },
//                    onBack = {
//                        println("Back clicked")
//                    }
//                )
//
//                InternetBillConfirmationScreen()
//                InternetBillSuccessScreen()
//
//
//                ElectricBillInputScreen()
//                ElectricBillConfirmationScreen()
//                ElectricBillSuccessScreen()
//
//
//                SaveOnlineScreen()
//                SaveOnlineSuccessScreen()
//                AdminScreen()
//                EditProfileScreen()
//                UpdatePasswordScreen()
//                FaceIdSettingScreen()
//
//
//                var selectedTab by remember { mutableStateOf(0) }
//
//                when (selectedTab) {
//                    0 -> DashboardScreen(
//                        userName = "Tran Pham Huu Phuc",
//                        selectedTab = selectedTab,
//                        onTabSelected = { selectedTab = it }
//                    )
//                    1 -> QRScreen()
//                    2 -> SettingScreen(
//                        selectedTab = selectedTab,
//                        onTabSelected = { selectedTab = it }
//                    )
//                }
            }
        }
    }
}
