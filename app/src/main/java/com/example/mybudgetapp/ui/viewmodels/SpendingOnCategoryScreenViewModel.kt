package com.example.mybudgetapp.ui.viewmodels

import android.net.Uri
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybudgetapp.data.capitalized
import com.example.mybudgetapp.data.formatCurrencyIraqiDinar
import com.example.mybudgetapp.database.ItemRepository
import com.example.mybudgetapp.database.ItemWithPurchaseDetails
import com.example.mybudgetapp.database.ItemWithPurchaseDetailsDao
import com.example.mybudgetapp.ui.screens.SpendingOnCategoryDestination
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.LocalDate
import java.time.Month
import java.util.Date
import java.util.Locale

class SpendingOnCategoryScreenViewModel(
    private val itemRepository: ItemRepository,
    savedStateHandle: SavedStateHandle
): ViewModel(){

    val date: LocalDate = LocalDate.now()
    private val _totalSpending = MutableStateFlow(0.0)

    private var _totalCategory = MutableStateFlow(0.0)
    private val currentMonth: String = checkNotNull(savedStateHandle[SpendingOnCategoryDestination.month])
    private val category: String = checkNotNull(savedStateHandle[SpendingOnCategoryDestination.category])
    private val currentMonthValue = getMonthNumber(currentMonth)
    private var isThisMonthCurrent: Boolean = true

    init {
        fetchTotals()
        isThisMonthCurrent = (date.monthValue == currentMonthValue)
    }



    val uiState: StateFlow<SpendingOnCategoryUiState> = combine (
        //fetching the items that will be displayed in the cards
            itemRepository.getItemWithPurchaseDetailsForCategory(month = currentMonthValue!! , year = date.year, category =  category),
        //fetching the total for the selected category
        itemRepository.getTotalSpendingOnCategory(
            month = currentMonthValue,
            year = date.year,
            category = category
        ),
        //fetching the total for the overall spending
        itemRepository.getTotalSpendingOverall(
            month = currentMonthValue,
            year = date.year,
        )
    ) { itemList, totalCategory, totalSpending ->
        SpendingOnCategoryUiState(
            totalSpending = formatCurrencyIraqiDinar(totalSpending),
            totalCategory = formatCurrencyIraqiDinar(totalCategory),
            spendingRatio = totalCategory.toFloat()/totalSpending.toFloat(),
            itemList = itemList.map { it.toSpendingOnCategoryItem() },
            isThisMonthCurrent = isThisMonthCurrent,
            category = category.capitalized()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = SpendingOnCategoryUiState()
    )


    private fun getMonthNumber(monthName: String): Int? {
        return try {
            val month = Month.valueOf(monthName.uppercase(Locale.ROOT)) // Convert the month name to uppercase and get the corresponding Month enum
            month.value // Get the value of the Month enum, which represents the month number (1 for January, 2 for February, etc.)
        } catch (e: IllegalArgumentException) {
            null // Return null if the month name is invalid
        }
    }



    private fun fetchTotals() {
        viewModelScope.launch {
            _totalCategory.value = itemRepository.getTotalSpendingOnCategory(
                month = currentMonthValue!!,
                year = date.year,
                category = category
            ).first()

            _totalSpending.value = itemRepository.getTotalSpendingOverall(
                month = currentMonthValue,
                year = date.year,
            ).first()
        }
    }


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}




data class SpendingOnCategoryUiState(
    val isThisMonthCurrent: Boolean = true,
    val category: String = "",
    val totalSpending: String = "",
    val totalCategory: String = "",
    val spendingRatio: Float = 0f,
    val itemList: List<SpendingOnCategoryItem> = listOf()
)

data class SpendingOnCategoryItem(
    val imagePath: String? = null,
    val name: String = "",
    val date: String = "",
    val totalCost: String = ""
)

fun ItemWithPurchaseDetails.toSpendingOnCategoryItem() : SpendingOnCategoryItem =
     SpendingOnCategoryItem(
         imagePath = item.picturePath,
         name = item.name,
         date = item.date,
         totalCost = formatCurrencyIraqiDinar(totalCost)
     )

