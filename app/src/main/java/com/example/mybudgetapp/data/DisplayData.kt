package com.example.mybudgetapp.data

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.mybudgetapp.R

data class SpendingCategoryDisplayData(
   @StringRes val title: Int,
   @ColorRes val cardColor: Int,
   @DrawableRes val spendingIcon: Int,
    val test: String
)

object SpendingCategoryDisplayObject {
    val items = listOf(
        SpendingCategoryDisplayData (
            title = R.string.food_spending,
            cardColor = R.color.pink,
            spendingIcon = R.drawable.baseline_fastfood_24,
            test = "as"
        ),
        SpendingCategoryDisplayData (
            title = R.string.transportation_spending,
            cardColor = R.color.pearly,
            spendingIcon = R.drawable.baseline_directions_transit_24,
            test = "bs"
        ),
        SpendingCategoryDisplayData (
            title = R.string.other_spending,
            cardColor = R.color.orange,
            spendingIcon = R.drawable.baseline_cookie_24,
            test = "cs"
        ),
        SpendingCategoryDisplayData (
            title = R.string.income,
            cardColor = R.color.income,
            spendingIcon = R.drawable.baseline_attach_money_25,
            test = "ks"
        ),
    )
}