package com.example.mybudgetapp.ui.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Month
import java.util.Locale

class ThisMonthScreenViewModel( 
    private val itemRepository: ItemRepository
) : ViewModel() {

    val date: LocalDate = LocalDate.now()

    private var selectedMonth = MutableStateFlow(date.monthValue)
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ThisMonthScreenUiState> = selectedMonth.flatMapLatest { month ->
        combine(
            itemRepository.getTotalSpendingOverall(
                month = month,
                year = date.year,
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
            ),
            itemRepository.getAllMonths(date.year)
        ) { totalSpending, totalFood, totalTransportation, totalOthers, months ->
            ThisMonthScreenUiState(
                totalSpending = formatCurrencyIraqiDinar(totalSpending),
                totalIncome = formatCurrencyIraqiDinar(totalSpending),
                totalSpendingOnFood = formatCurrencyIraqiDinar(totalFood),
                totalSpendingOnOthers = formatCurrencyIraqiDinar(totalOthers),
                totalSpendingOnTransportation = formatCurrencyIraqiDinar(totalTransportation),
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