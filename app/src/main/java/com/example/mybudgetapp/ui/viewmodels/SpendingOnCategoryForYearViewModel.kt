package com.example.mybudgetapp.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybudgetapp.data.capitalized
import com.example.mybudgetapp.data.formatCurrencyIraqiDinar
import com.example.mybudgetapp.database.ItemRepository
import com.example.mybudgetapp.ui.screens.SpendingOnCategoryForYearDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class SpendingOnCategoryForYearScreenViewModel(
    private val itemRepository: ItemRepository,
    savedStateHandle: SavedStateHandle
): ViewModel(){

    val date: LocalDate = LocalDate.now()
    private val currentYear: Int = checkNotNull(savedStateHandle[SpendingOnCategoryForYearDestination.year.toString()])
    private val category: String = checkNotNull(savedStateHandle[SpendingOnCategoryForYearDestination.category])
    private var isThisYearCurrent: Boolean = true
    private var isDeleteDialogVisible = MutableStateFlow(false)


    init {
        isThisYearCurrent = (date.year == currentYear)
    }



    val uiState: StateFlow<SpendingOnCategoryUiState> = combine (
        //fetching the items that will be displayed in the cards
        itemRepository.getItemWithPurchaseDetailsForCategoryForYear(year = currentYear, category =  category),
        //fetching the total for the selected category
        itemRepository.getTotalSpendingOnCategoryForYear(
            year = currentYear,
            category = category
        ),
        //fetching the total for the overall spending
        itemRepository.getTotalSpendingOverallForYear(
            year = currentYear,
        )
    ) { itemList, totalCategory, totalSpending ->
        SpendingOnCategoryUiState(
            totalSpending = formatCurrencyIraqiDinar(totalSpending),
            totalCategory = formatCurrencyIraqiDinar(totalCategory),
            spendingRatio = totalCategory.toFloat()/totalSpending.toFloat(),
            itemList = itemList.map { it.toSpendingOnCategoryItem() },
            isThisMonthCurrent = isThisYearCurrent,
            category = category.capitalized(),
            sentCategory = category,
            isDeleteDialogVisible = isDeleteDialogVisible.value

        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = SpendingOnCategoryUiState()
    )



    fun deleteItem(itemId: Long){
        viewModelScope.launch {
            itemRepository.deleteItemWithId(itemId)
        }
    }

    fun displayConfirmDelete(isIt: Boolean) {
        isDeleteDialogVisible.value = isIt

    }





    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}




