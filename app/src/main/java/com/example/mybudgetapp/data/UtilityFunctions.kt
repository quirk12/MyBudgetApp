package com.example.mybudgetapp.data

import java.text.NumberFormat
import java.util.Locale

fun formatCurrencyIraqiDinar(amount: Double): String {
    val locale = Locale("EN", "IQ") // You can change the locale based on your requirements
    val currencyFormat = NumberFormat.getCurrencyInstance(locale)

    return currencyFormat.format(amount)
}
fun String.capitalized(): String {
    return this.substring(0, 1).uppercase(Locale.ROOT) + this.substring(1).lowercase(Locale.ROOT)
    }

