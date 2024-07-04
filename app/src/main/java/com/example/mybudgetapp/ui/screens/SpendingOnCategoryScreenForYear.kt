package com.example.mybudgetapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.example.mybudgetapp.ui.viewmodels.SpendingOnCategoryForYearScreenViewModel
import com.example.mybudgetapp.ui.widgets.BudgetTopAppBar

object SpendingOnCategoryForYearDestination: NavigationDestination {
    override val route = "SpendingOnCategory"
    override val titleRes = R.string.spending_on_category_screen
    const val category = "category"
    const val year = 0
    val routeWithArgs = "$route/{$category}/{$year}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpendingOnCategoryScreenForYear(
    modifier: Modifier = Modifier,
    navigateToAddItem: (String) -> Unit,
    navigateBack: () -> Unit,
    navigateToItemDates: (Long) -> Unit
) {
    val scrollState = rememberLazyListState() // Assuming a LazyColumn
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val viewModel: SpendingOnCategoryForYearScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val uiState = viewModel.uiState.collectAsState()
    val context = LocalContext.current
    Scaffold (
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar =  {
            BudgetTopAppBar(
                canNavigateBack = true,
                title = stringResource(id = R.string.spending_on_category_screen,
                    uiState.value.category
                ),
                scrollBehavior = scrollBehavior,
                navigateBack = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (uiState.value.isThisMonthCurrent) {navigateToAddItem(uiState.value.sentCategory)}
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
        SpendingOnCategoryBody(
            Modifier.padding(paddingValues),
            uiState = uiState.value,
            deleteItem = {viewModel.deleteItem(it)},
            navigateToItemDates = {navigateToItemDates(it)},
        )
    }
}


