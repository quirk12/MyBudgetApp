package com.example.mybudgetapp.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybudgetapp.data.formatCurrencyIraqiDinar
import com.example.mybudgetapp.database.ItemRepository
import com.example.mybudgetapp.database.ItemWithPurchaseDetails
import com.example.mybudgetapp.ui.screens.TotalIncomeDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class TotalSpendingScreenViewModel(
    private val itemRepository: ItemRepository,
    savedStateHandle: SavedStateHandle
): ViewModel(){

    val date: LocalDate = LocalDate.now()
    private val currentMonth: String = checkNotNull(savedStateHandle[TotalIncomeDestination.month])
    private val isIncome: Boolean = checkNotNull(savedStateHandle[TotalIncomeDestination.isIncome.toString()])
    private val currentMonthValue = getMonthNumber(currentMonth)
    private var isThisMonthCurrent = true
    private var isDeleteDialogVisible = MutableStateFlow(false)

    init {
        isThisMonthCurrent = (date.monthValue == currentMonthValue)
    }

    val uiState: StateFlow<TotalSpendingUiState> = combine(
        itemRepository.getItemWithPurchaseDetails(month = currentMonthValue!!, year = date.year),
        itemRepository.getTotalSpendingOverall(year = date.year, month = currentMonthValue),
        itemRepository.getTotalIncomeOverall(year = date.year, month = currentMonthValue),
        itemRepository.getItemWithPurchaseDetailsForCategory(category = "income", year = date.year, month = currentMonthValue)
        ){   spendingItems, totalSpending, totalIncome, incomeItems  ->
            TotalSpendingUiState(
                totalSpending = formatCurrencyIraqiDinar(totalSpending),
                spendingItemList = spendingItems.map {it.toSpendingItem()},
                month = currentMonth,
                isIncome = isIncome,
                totalIncome = formatCurrencyIraqiDinar(totalIncome),
                incomeItemList = incomeItems.map { it.toSpendingItem() },
                isThisMonthCurrent = isThisMonthCurrent,
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


data class TotalSpendingUiState(
    val isDeleteDialogVisible: Boolean = false,
    val isThisMonthCurrent: Boolean = true,
    val totalSpending: String = "",
    val month: String = "",
    val isIncome: Boolean = true,
    val totalIncome: String = "",
    val spendingItemList: List<SpendingItem> = listOf(),
    val incomeItemList: List<SpendingItem> = listOf()
)

data class SpendingItem(
    val imagePath: String? = null,
    val name: String = "",
    val date: String = "",
    val totalCost: String = "",
    val category:String = "",
    val itemId: Long = 0
)
fun ItemWithPurchaseDetails.toSpendingItem(): SpendingItem =
    SpendingItem(
        imagePath = item.picturePath,
        name = item.name,
        date = item.date,
        totalCost = formatCurrencyIraqiDinar(totalCost),
        category = item.category,
        itemId = item.itemId
    )