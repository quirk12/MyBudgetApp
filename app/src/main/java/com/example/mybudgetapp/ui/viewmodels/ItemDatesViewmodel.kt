package com.example.mybudgetapp.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybudgetapp.data.formatCurrencyIraqiDinar
import com.example.mybudgetapp.database.ItemRepository
import com.example.mybudgetapp.database.PurchaseDetails
import com.example.mybudgetapp.ui.screens.ItemDatesScreenNavigationDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ItemDatesViewModel(
    private val itemRepository: ItemRepository,
    savedStateHandle: SavedStateHandle
): ViewModel(){


    private val id: Long = checkNotNull(savedStateHandle[ItemDatesScreenNavigationDestination.id.toString()])


    val uiState: StateFlow<ItemDatesUiState> = combine(
        itemRepository.getItemDates(id),
        itemRepository.getItemFromId(id)
    ) { dates, item ->
        ItemDatesUiState (
            itemDatesList = dates.map { it.toItemWithDates() },
            category = item.category,
            name = item.name,
            date = item.date,
            picturePath = item.picturePath
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = ItemDatesUiState()
    )




    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}


data class ItemDatesUiState(
    val itemDatesList: List<ItemWIthDates> = listOf(),
    val category: String = "",
    val picturePath: String? = null,
    val date: String = "",
    val name: String = "",
)

data class ItemWIthDates (
    val cost: String = "",
    val date: String = "",
)

fun PurchaseDetails.toItemWithDates(): ItemWIthDates =
    ItemWIthDates(
        cost = formatCurrencyIraqiDinar(cost),
        date = purchaseDate
    )



