package com.example.mybudgetapp.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybudgetapp.data.formatCurrencyIraqiDinar
import com.example.mybudgetapp.database.ItemRepository
import com.example.mybudgetapp.ui.screens.TotalIncomeDestinationForYear
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class TotalSpendingScreenForYearViewModel(
    private val itemRepository: ItemRepository,
    savedStateHandle: SavedStateHandle
): ViewModel(){

    val date: LocalDate = LocalDate.now()
    private val currentYear: Int = checkNotNull(savedStateHandle[TotalIncomeDestinationForYear.year.toString()])
    private val isIncome: Boolean = checkNotNull(savedStateHandle[TotalIncomeDestinationForYear.isIncome.toString()])
    private var isThisYearCurrent = true
    private var isDeleteDialogVisible = MutableStateFlow(false)
    init {
        isThisYearCurrent = (date.year == currentYear)
    }

    val uiState: StateFlow<TotalSpendingUiState> = combine(
        itemRepository.getItemWithPurchaseDetailsForYear(year = currentYear),
        itemRepository.getTotalSpendingOverallForYear(year = currentYear),
        itemRepository.getTotalIncomeOverallForYear(year = currentYear),
        itemRepository.getItemWithPurchaseDetailsForCategoryForYear(category = "income", year = currentYear)
    ){   spendingItems, totalSpending, totalIncome, incomeItems  ->
        TotalSpendingUiState(
            totalSpending = formatCurrencyIraqiDinar(totalSpending),
            spendingItemList = spendingItems.map {it.toSpendingItem()},
            month = currentYear.toString(),
            isIncome = isIncome,
            totalIncome = formatCurrencyIraqiDinar(totalIncome),
            incomeItemList = incomeItems.map { it.toSpendingItem() },
            isThisMonthCurrent = isThisYearCurrent,
            isDeleteDialogVisible = isDeleteDialogVisible.value
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = TotalSpendingUiState()
    )

    fun deleteItem(itemId: Long) {
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

