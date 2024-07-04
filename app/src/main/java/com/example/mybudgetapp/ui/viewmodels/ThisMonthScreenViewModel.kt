package com.example.mybudgetapp.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybudgetapp.data.capitalized
import com.example.mybudgetapp.data.formatCurrencyIraqiDinar
import com.example.mybudgetapp.database.ItemRepository
import com.example.mybudgetapp.ui.widgets.DropDownItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.Month

class ThisMonthScreenViewModel( 
    private val itemRepository: ItemRepository
) : ViewModel() {

    val date: LocalDate = LocalDate.now()

    private var selectedMonth = MutableStateFlow(date.monthValue)
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ThisMonthScreenUiState> = selectedMonth.flatMapLatest { month ->
        combine(
            combine(
                itemRepository.getTotalSpendingOverall(
                    month = month,
                    year = date.year,
                ),
                itemRepository.getTotalIncomeOverall(
                    year = date.year, month = month
                ),
                itemRepository.getTotalSpendingOnCategory(
                    month = month,
                    year = date.year,
                    category = "food"
                ),
                itemRepository.getTotalSpendingOnCategory(
                    month = month,
                    year = date.year,
                    category = "transportation"
                ),
                itemRepository.getTotalSpendingOnCategory(
                    month = month,
                    year = date.year,
                    category = "others"
                )
            ) { totalSpending, totalIncome, totalFood, totalTrans, totalOther ->
                listOf(totalSpending, totalIncome, totalFood, totalTrans, totalOther)
            },
            itemRepository.getAllMonths(date.year)
        ) { tuple, months ->
            ThisMonthScreenUiState(
                totalSpending = formatCurrencyIraqiDinar(tuple[0]),
                totalIncome = formatCurrencyIraqiDinar(tuple[1]),
                totalSpendingOnFood = formatCurrencyIraqiDinar(tuple[2]),
                totalSpendingOnOthers = formatCurrencyIraqiDinar(tuple[4]),
                totalSpendingOnTransportation = formatCurrencyIraqiDinar(tuple[3]),
                currentMonth = Month.of(month).toString().capitalized(),
                months = months.map {
                    DropDownItem(
                        title = (Month.of(it).toString()).capitalized(),
                        number = it
                    )
                },
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(ThisMonthScreenViewModel.TIMEOUT_MILLIS),
        initialValue = ThisMonthScreenUiState()
    )





    fun updateVisibility(screenItemsUiState: ScreenItemsUiState) {
      uiState.value.screenItemsUiState.value = screenItemsUiState

    }
    fun updateSelectedMonth(month: Int) {
            selectedMonth.value = month

    }




    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}

data class ScreenItemsUiState(
    val isDropDownMenuVisible: Boolean = false,
    val itemHeight: Dp = 0.dp,
    val offSet: DpOffset = DpOffset.Zero,

)


data class ThisMonthScreenUiState(
    val currentMonth: String = "",
    val totalSpending: String = "",
    val totalIncome: String = "",
    val totalSpendingOnFood: String = "",
    val totalSpendingOnTransportation: String = "",
    val totalSpendingOnOthers: String = "",
    val months: List<DropDownItem> = listOf(),
    val screenItemsUiState: MutableState<ScreenItemsUiState> = mutableStateOf(ScreenItemsUiState())

)