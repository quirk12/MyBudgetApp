package com.example.mybudgetapp.data

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.mybudgetapp.R

data class SpendingCategoryDisplayData(
   @StringRes val title: Int,
   @ColorRes val cardColor: Int,
   @DrawableRes val spendingIcon: Int
)

object SpendingCategoryDisplayObject {
    val items = listOf(
        SpendingCategoryDisplayData (
            title = R.string.food_spending,
            cardColor = R.color.pink,
            spendingIcon = R.drawable.baseline_fastfood_24
        ),
        SpendingCategoryDisplayData (
            title = R.string.transportation_spending,
            cardColor = R.color.pearly,
            spendingIcon = R.drawable.baseline_directions_transit_24
        ),
        SpendingCategoryDisplayData (
            title = R.string.other_spending,
            cardColor = R.color.orange,
            spendingIcon = R.drawable.baseline_cookie_24
        ),
    )
}