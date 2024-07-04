package com.example.mybudgetapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mybudgetapp.R
import com.example.mybudgetapp.data.SpendingCategoryDisplayObject
import com.example.mybudgetapp.ui.navigation.NavigationDestination
import com.example.mybudgetapp.ui.theme.dmSans
import com.example.mybudgetapp.ui.viewmodels.AppViewModelProvider
import com.example.mybudgetapp.ui.viewmodels.TotalSpendingScreenViewModel
import com.example.mybudgetapp.ui.viewmodels.TotalSpendingUiState
import com.example.mybudgetapp.ui.widgets.BudgetTopAppBar
import com.example.mybudgetapp.ui.widgets.ItemCard
import com.example.mybudgetapp.ui.widgets.TotalIncomeCard

object TotalIncomeDestination: NavigationDestination {
    override val route = "TotalIncome"
    override val titleRes = R.string.total_income_screen
    const val month = "this"
    const val isIncome: Boolean = true
    val routeWithArgs = "$route/{$month}/{$isIncome}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TotalIncomeScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateToAddItem: (String) -> Unit,
    navigateToItemDates: (Long) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val viewModel: TotalSpendingScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val uiState = viewModel.uiState.collectAsState()
    val context = LocalContext.current
    Scaffold (
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { BudgetTopAppBar(
            canNavigateBack = true,
            title = stringResource(id = R.string.total_income_screen, if(uiState.value.isIncome) "Income" else "Spending") ,
            navigateBack = navigateBack,
            scrollBehavior = scrollBehavior
        )},
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (uiState.value.isThisMonthCurrent) {
                        navigateToAddItem(
                            if (uiState.value.isIncome) "income" else "all"
                                )
                    }
                    else {
                        Toast.makeText(context, R.string.you_cant_add_item_archived, Toast.LENGTH_SHORT).show()
                    }
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(20.dp),
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    ) {paddingValues ->
        TotalIncomeBody(
            Modifier.padding(paddingValues),
            uiState = uiState.value,
            deleteItem = {
                viewModel.deleteItem(it)
            },
            navigateToItemDates = {navigateToItemDates(it)},
        )
    }
}

@Composable
fun TotalIncomeBody(
    modifier: Modifier = Modifier,
    uiState: TotalSpendingUiState,
    deleteItem: (Long) -> Unit,
    navigateToItemDates: (Long) -> Unit,
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 18.dp, horizontal = 12.dp)
            .then(modifier),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        LazyColumn {
            item {
                TotalIncomeCard(
                    modifier = Modifier.padding(bottom = 32.dp),
                    title = if(uiState.isIncome) R.string.total_income_in else R.string.total_spending_in,
                    totalSpending = if(uiState.isIncome) uiState.totalIncome else uiState.totalSpending,
                    month = uiState.month
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .padding(vertical = 2.dp, horizontal = 12.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.total_income_screen_body, if (uiState.isIncome) "Income" else "Spending"),
                        fontFamily = dmSans,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            }
            items(
                if(uiState.isIncome) uiState.incomeItemList else uiState.spendingItemList
            ) {item ->
                ItemCard(
                    title = item.name,
                    totalSpending = item.totalCost,
                    modifier = Modifier.padding(bottom = 12.dp),
                    deleteItem = {deleteItem(item.itemId)} ,
                    date = item.date,
                    imagePath = item.imagePath,
                    navigateToItemDates = {navigateToItemDates(item.itemId)},
                    displayItem =
                    when (item.category) {
                        "food" -> SpendingCategoryDisplayObject.items[0]
                        "others" -> SpendingCategoryDisplayObject.items[2]
                        "transportation" -> SpendingCategoryDisplayObject.items[1]
                        else -> SpendingCategoryDisplayObject.items[3]
                    },
                )
            }
        }
    }
}

