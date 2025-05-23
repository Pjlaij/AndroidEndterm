package com.example.mobilebanking.helper

import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private const val TMN_CODE   = "EP87GC08"
private const val HASH_SECRET= "K3MI6GS881TMYMQE2V3Q02TOTOX6CRFN"
private const val VNP_URL    = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html"
private const val RETURN_URL = "http://success.sdk.merchantbackapp"

/** urlencode PHP-style (space -> +) */
private fun enc(value: String) =
    URLEncoder.encode(value, StandardCharsets.UTF_8.name())

fun makePayUrlPhpStyle(amount: Long): String {
    val txnRef = (System.currentTimeMillis() % 1_000_000_000).toString()
    val createDate = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())

    val params = sortedMapOf(
        "vnp_Version"   to "2.1.0",
        "vnp_TmnCode"   to TMN_CODE,
        "vnp_Amount"    to (amount * 100).toString(),
        "vnp_Command"   to "pay",
        "vnp_CreateDate" to createDate,
        "vnp_CurrCode"  to "VND",
        "vnp_IpAddr"    to "0.0.0.0",
        "vnp_Locale"    to "vn",
        "vnp_OrderInfo" to txnRef,
        "vnp_OrderType" to "other",
        "vnp_ReturnUrl" to RETURN_URL,
        "vnp_TxnRef"    to txnRef
    )

    /* 1. hashdata và query (urlencode cả key & value) */
    var hashData = ""
    var query = ""
    params.entries.forEachIndexed { idx, (k, v) ->
        val ampersand = if (idx == 0) "" else "&"
        hashData += ampersand + enc(k) + "=" + enc(v)
        query    += ampersand + enc(k) + "=" + enc(v)
    }

    /* 2. SHA-512(secret + hashData) */
    val mac = Mac.getInstance("HmacSHA512")
    mac.init(SecretKeySpec(HASH_SECRET.toByteArray(), "HmacSHA512"))
    val secureHash = mac.doFinal(hashData.toByteArray())
        .joinToString("") { "%02x".format(it) }

    /* 3. URL cuối */
    return "$VNP_URL?$query&vnp_SecureHash=$secureHash"
}