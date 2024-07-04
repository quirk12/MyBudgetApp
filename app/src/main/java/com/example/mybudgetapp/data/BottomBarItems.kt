package com.example.mybudgetapp.data

import androidx.annotation.DrawableRes
import com.example.mybudgetapp.R

data class BottomNavigationItem(
    val title: String,
  @DrawableRes  val selectedIcon: Int,
    @DrawableRes val unSelectedIcon: Int,
    val hasNews: Boolean
)

object NavigationItems {
    val items = listOf(
        BottomNavigationItem(
            title = "This Year",
            selectedIcon = R.drawable.baseline_view_timeline_24,
            unSelectedIcon = R.drawable.outline_view_timeline_24,
            hasNews = false
        ),
        BottomNavigationItem(
            title = "This Month",
            selectedIcon = R.drawable.baseline_calendar_month_24,
            unSelectedIcon = R.drawable.outline_calendar_month_24,
            hasNews = false
        ),

    )
}