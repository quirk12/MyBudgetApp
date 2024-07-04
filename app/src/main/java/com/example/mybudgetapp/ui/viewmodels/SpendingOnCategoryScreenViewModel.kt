package com.example.mybudgetapp.ui.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybudgetapp.data.capitalized
import com.example.mybudgetapp.data.formatCurrencyIraqiDinar
import com.example.mybudgetapp.database.ItemRepository
import com.example.mybudgetapp.database.ItemWithPurchaseDetails
import com.example.mybudgetapp.ui.screens.SpendingOnCategoryDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Month
import java.util.Locale

class SpendingOnCategoryScreenViewModel(
    private val itemRepository: ItemRepository,
    savedStateHandle: SavedStateHandle
): ViewModel(){

    val date: LocalDate = LocalDate.now()
    private val currentMonth: String = checkNotNull(savedStateHandle[SpendingOnCategoryDestination.month])
    private val category: String = checkNotNull(savedStateHandle[SpendingOnCategoryDestination.category])
    private val currentMonthValue = getMonthNumber(currentMonth)
    private var isThisMonthCurrent: Boolean = true
    private var isDeleteDialogVisible = MutableStateFlow(false)


    init {
        isThisMonthCurrent = (date.monthValue == currentMonthValue)
    }



    var uiState: StateFlow<SpendingOnCategoryUiState> = combine (
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

    fun displayConfirmDelete(isIt: Boolean, context: Context) {
        isDeleteDialogVisible.value = isIt
        Toast.makeText(context, "this is ${uiState.value.isDeleteDialogVisible} but $isIt", Toast.LENGTH_SHORT ).show()

    }





    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}




data class SpendingOnCategoryUiState(
    val isDeleteDialogVisible: Boolean = false,
    val isThisMonthCurrent: Boolean = true,
    val category: String = "",
    val sentCategory: String = "",
    val totalSpending: String = "",
    val totalCategory: String = "",
    val spendingRatio: Float = 0f,
    val itemList: List<SpendingOnCategoryItem> = listOf()
)

data class SpendingOnCategoryItem(
    val imagePath: String? = null,
    val name: String = "",
    val date: String = "",
    val totalCost: String = "",
    val itemId: Long = 0
)

fun ItemWithPurchaseDetails.toSpendingOnCategoryItem() : SpendingOnCategoryItem =
     SpendingOnCategoryItem(
         itemId = item.itemId ,
         imagePath = item.picturePath,
         name = item.name,
         date = item.date,
         totalCost = formatCurrencyIraqiDinar(totalCost)
     )
fun getMonthNumber(monthName: String): Int? {
    return try {
        val month = Month.valueOf(monthName.uppercase(Locale.ROOT)) // Convert the month name to uppercase and get the corresponding Month enum
        month.value // Get the value of the Month enum, which represents the month number (1 for January, 2 for February, etc.)
    } catch (e: IllegalArgumentException) {
        null // Return null if the month name is invalid
    }
}

