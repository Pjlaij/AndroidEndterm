package com.example.mobilebanking.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mobilebanking.model.*
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.database.FirebaseDatabase
import java.sql.Date
import java.time.LocalDate


class UserInfoViewModel : ViewModel() {
    var userInfo by mutableStateOf<UserInfo?>(null)
        private set

 //// Network Call///////////////////////////////////////////////////////////////////
//    private fun loadUserInfo() {
//        viewModelScope.launch {
//            delay(1000) // simulate network/database delay
//            userInfo = UserInfo(
//                name = "Tran Pham Huu Phuc",
//                username = "user@example.com",
//                password = "password",
//                phone = "0947333446",
//                accountNumber = "1027231846",
//                balance = 2000000,
//                electricBill = listOf(ElectricBill("2343543","TRAN PHAM HUU PHUC","403 East 4th Street", "094733446", 470, 10, false, "01/10/2019", "01/11/2019")),
//                internetBill = listOf(InternetBill("2343543","TRAN PHAM HUU PHUC","403 East 4th Street", "094733446", 470, 10, false, "01/10/2019", "01/11/2019")),
//                waterBill = listOf(WaterBill("01",2000, false, "07/05/2025")),
//                savingAccount = SavingAccount("01",200000,"01/01/2024","01/01/2026","6%","12 tháng"),
//                mortgage = Mortgage(-180000000,5000000),
//                history = listOf(null)
//            )
//
//
//        }
//    }
 fun loadUserInfo(context: Context) {
     viewModelScope.launch {
         val prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
         val uid = prefs.getString("uid", null)

         if (!uid.isNullOrEmpty()) {
             FirebaseDatabase.getInstance().getReference("users")
                 .child(uid)
                 .get()
                 .addOnSuccessListener { snapshot ->
                     if (snapshot.exists()) {
                         val user = snapshot.getValue(UserInfo::class.java)
                         if (user != null) {
                             userInfo = user
                         } else {
                             println("User data is null for UID: $uid")
                         }
                     } else {
                         println("No user found for UID: $uid")
                     }
                 }
                 .addOnFailureListener { error ->
                     println("Failed to load user info: ${error.message}")
                 }
         } else {
             println("No UID found in SharedPreferences")
         }
     }
 }
/////////////////////////////////////////////////////////////////////////////
public fun checkElectricBill(billCode: String?): ElectricBill? {
    val bills = userInfo?.electricBill ?: return null
    for (bill in bills) {
        if (bill.electricBillId.trim() == billCode?.trim()) {
            return bill
        }
    }
    return null


}

    public fun checkInternetBill(billCode: String?): InternetBill? {
        val bills = userInfo?.internetBill ?: return null
        for (bill in bills) {
            if (bill.internetBillId.trim() == billCode?.trim()) {
                return bill
            }
        }
        return null


    }

}



//data class UserInfo(
//    var name: String = "",
//    var username: String = "",
//    var password: String = "",
//    var phone: String = "",
//    var accountNumber: String = "",
//    var balance: Int = 0,
//    var electricBill: List<ElectricBill> = listOf(),
//    var internetBill: List<InternetBill> = listOf(),
//    var waterBill: List<WaterBill> = listOf(),
//    var savingAccount: SavingAccount = SavingAccount(),
//    var mortgage: Mortgage? = null,
//    val history: Map<String, History> = mapOf()
//)
//
data class History(
    val from: String = "",
    val to: String = "",
    val amount: Int = 0,
    val type: String = "",
    val content: String = "",
    val timestamp: Long = 0L
)
//
//
//data class WaterBill(
//    var waterBillId: String,
//    var billedAmount: Int,
//    var status: Boolean,
//    var date: String
//)
//
//data class SavingAccount(
//    var savingAccountId: String = "",
//    var savingAccountBalance: Int = -1,
//    var savingDateStart: String = "",
//    var savingDateEnd: String = "",
//    var interest: String = "",
//    var period: String = ""
//)
//
//data class Mortgage(
//    var mortgage: Int,
//    var monthlyPayment: Int
//)
//
//data class  ElectricBill(
//    var electricBillId: String = "",
//    var billedUserName: String = "",
//    var address: String = "",
//    var phoneNumber: String = "",
//    var billedAmount: Int = -1,
//    var tax: Int = -1,
//    var isPaid: Boolean = false,
//    var dateStart: String = "",
//    var dateEnd: String = ""
//)
//
//data class  InternetBill(
//    var internetBillId: String = "",
//    var billedUserName: String = "",
//    var address: String = "",
//    var phoneNumber: String = "",
//    var billedAmount: Int = -1,
//    var tax: Int = -1,
//    var isPaid: Boolean = false,
//    var dateStart: String = "",
//    var dateEnd: String = ""
//)