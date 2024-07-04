package com.example.mybudgetapp.ui.screens


import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mybudgetapp.R
import com.example.mybudgetapp.ui.navigation.NavigationDestination
import com.example.mybudgetapp.ui.viewmodels.AppViewModelProvider
import com.example.mybudgetapp.ui.viewmodels.TotalSpendingScreenForYearViewModel
import com.example.mybudgetapp.ui.widgets.BudgetTopAppBar

object TotalIncomeDestinationForYear: NavigationDestination {
    override val route = "TotalIncomeForYear"
    override val titleRes = R.string.total_income_screen
    const val year = 0
    const val isIncome: Boolean = true
    val routeWithArgs = "$route/{$year}/{$isIncome}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TotalIncomeScreenForYear(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateToAddItem: (String) -> Unit,
    navigateToItemDates: (Long) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val viewModel: TotalSpendingScreenForYearViewModel = viewModel(factory = AppViewModelProvider.Factory)
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
