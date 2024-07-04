package com.example.mybudgetapp.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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

class ThisYearScreenViewModel(
    private val itemRepository: ItemRepository
) : ViewModel() {

    val date: LocalDate = LocalDate.now()

    private var selectedMonth = MutableStateFlow(date.monthValue)
    private var selectedYear = MutableStateFlow(date.year)
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ThisYearScreenUiState> = selectedMonth.flatMapLatest { month ->
        combine(
            combine(
                itemRepository.getTotalSpendingOverallForYear(
                    year = selectedYear.value,
                ),
                itemRepository.getTotalIncomeOverallForYear(
                    year = selectedYear.value
                ),
                itemRepository.getTotalSpendingOnCategoryForYear(
                    year = selectedYear.value,
                    category = "food"
                ),
                itemRepository.getTotalSpendingOnCategoryForYear(
                    year = selectedYear.value,
                    category = "transportation"
                ),
                itemRepository.getTotalSpendingOnCategoryForYear(
                    year = selectedYear.value,
                    category = "others"
                )
            ) { totalSpending, totalIncome, totalFood, totalTrans, totalOther ->
                listOf(totalSpending, totalIncome, totalFood, totalTrans, totalOther)
            },
            itemRepository.getAllYears()
        ) { tuple, years ->
            ThisYearScreenUiState(
                totalSpendingForYear = formatCurrencyIraqiDinar(tuple[0]),
                totalIncomeForYear = formatCurrencyIraqiDinar(tuple[1]),
                totalSpendingOnFoodForYear = formatCurrencyIraqiDinar(tuple[2]),
                totalSpendingOnOthersForYear = formatCurrencyIraqiDinar(tuple[4]),
                totalSpendingOnTransportationForYear = formatCurrencyIraqiDinar(tuple[3]),
                currentMonth = Month.of(month).toString().capitalized(),
                currentYear = selectedYear.value.toString(),
                years = years.map {
                    DropDownItem(
                        title = (years.toString()).capitalized(),
                        number = it
                    )
                },
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = ThisYearScreenUiState()
    )





    fun updateVisibility(screenItemsUiState: ScreenItemsUiState) {
        uiState.value.screenItemsUiState.value = screenItemsUiState

    }
    fun updateSelectedMonth(year: Int) {
        selectedYear.value = year

    }




    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}



data class ThisYearScreenUiState(
    val currentMonth: String = "",
    val currentYear: String = "",
    val totalSpendingForYear: String = "",
    val totalIncomeForYear: String = "",
    val totalSpendingOnFoodForYear: String = "",
    val totalSpendingOnTransportationForYear: String = "",
    val totalSpendingOnOthersForYear: String = "",
    val years: List<DropDownItem> = listOf(),
    val screenItemsUiState: MutableState<ScreenItemsUiState> = mutableStateOf(ScreenItemsUiState())

)