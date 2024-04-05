package com.example.mybudgetapp.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mybudgetapp.R
import com.example.mybudgetapp.data.SpendingCategoryDisplayObject
import com.example.mybudgetapp.ui.navigation.NavigationDestination
import com.example.mybudgetapp.ui.theme.dmSans
import com.example.mybudgetapp.ui.viewmodels.AppViewModelProvider
import com.example.mybudgetapp.ui.viewmodels.SpendingOnCategoryItem
import com.example.mybudgetapp.ui.viewmodels.SpendingOnCategoryScreenViewModel
import com.example.mybudgetapp.ui.viewmodels.SpendingOnCategoryUiState
import com.example.mybudgetapp.ui.widgets.BudgetTopAppBar
import com.example.mybudgetapp.ui.widgets.ItemCard
import com.example.mybudgetapp.ui.widgets.SpendingProgress

object SpendingOnCategoryDestination: NavigationDestination {
    override val route = "SpendingOnCategory"
    override val titleRes = R.string.spending_on_category_screen
    const val category = "category"
    const val month = "this"
    val routeWithArgs = "$route/{$category}/{$month}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpendingOnCategoryScreen(
    modifier: Modifier = Modifier,
    navigateToAddItem: () -> Unit,
    navigateBack: () -> Unit
) {
    val scrollState = rememberLazyListState() // Assuming a LazyColumn
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val viewModel: SpendingOnCategoryScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val uiState = viewModel.uiState.collectAsState()
    val context = LocalContext.current
    Scaffold (
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar =  {
                BudgetTopAppBar(
                    canNavigateBack = true,
                    title = stringResource(id = R.string.spending_on_category_screen, uiState.value.category),
                    scrollBehavior = scrollBehavior,
                    navigateBack = navigateBack
                )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (uiState.value.isThisMonthCurrent) {navigateToAddItem()}
                    else {
                        Toast.makeText(context, R.string.you_cant_add_item_archived, Toast.LENGTH_SHORT).show()
                    }
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(20.dp)
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
                uiState = uiState.value
            )
    }
}


@Composable
fun SpendingOnCategoryBody(
    modifier: Modifier = Modifier,
    uiState: SpendingOnCategoryUiState
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
                    SpendingProgress(
                        modifier = Modifier.padding(bottom = 32.dp),
                        totalSpending = uiState.totalSpending,
                        totalSpendingOnCategory = uiState.totalCategory,
                        spendingRatio = uiState.spendingRatio,
                        category = uiState.category

                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .padding(vertical = 2.dp, horizontal = 12.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Your spending",
                            fontFamily = dmSans,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }
                }
                items(uiState.itemList) { it ->
                    ItemCard(
                        title = it.name,
                        totalSpending = it.totalCost,
                        modifier = Modifier.padding(bottom = 12.dp),
                        item = SpendingCategoryDisplayObject.items[0],
                        date = it.date,
                        imagePath = it.imagePath,
                        displayItem =
                        when (uiState.category) {
                            "food" -> SpendingCategoryDisplayObject.items[0]
                            "others" -> SpendingCategoryDisplayObject.items[2]
                            else -> SpendingCategoryDisplayObject.items[1]
                        }

                        )
                }
        }
    }
}

