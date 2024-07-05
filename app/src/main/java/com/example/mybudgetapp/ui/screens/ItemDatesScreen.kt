package com.example.mybudgetapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.mybudgetapp.ui.viewmodels.ItemDatesUiState
import com.example.mybudgetapp.ui.viewmodels.ItemDatesViewModel
import com.example.mybudgetapp.ui.widgets.BudgetTopAppBar
import com.example.mybudgetapp.ui.widgets.DateCard
import com.example.mybudgetapp.ui.widgets.ItemCardForDates

object ItemDatesScreenNavigationDestination: NavigationDestination {
    override val route = "ItemDates"
    override val titleRes = R.string.spending_on_category_screen
    const val id: Long = 0
    const val imagePath: String = ""
    const val name: String = ""
    val routeWithArgs = "$route/{$id}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDatesScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit
) {
    val scrollState = rememberLazyListState() // Assuming a LazyColumn
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val viewModel: ItemDatesViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val uiState = viewModel.uiState.collectAsState()
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            BudgetTopAppBar(
                canNavigateBack = true,
                title = stringResource(id = R.string.item_dates),
                scrollBehavior = scrollBehavior,
                navigateBack = navigateBack
            )
        },
    ) { paddingValues ->

        ItemDatesBody(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState.value
        )

    }
}

@Composable
fun ItemDatesBody(
    modifier: Modifier = Modifier,
    uiState: ItemDatesUiState,
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
                ItemCardForDates(
                    title = uiState.name,
                    displayItem =
                    when (uiState.category) {
                        "food" -> SpendingCategoryDisplayObject.items[0]
                        "others" -> SpendingCategoryDisplayObject.items[2]
                        "transportation" -> SpendingCategoryDisplayObject.items[1]
                        else -> SpendingCategoryDisplayObject.items[3]
                    },
                    imagePath = uiState.picturePath,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
            }
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .padding(vertical = 2.dp, horizontal = 12.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.item_dates_body),
                        fontFamily = dmSans,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            }





            items(uiState.itemDatesList) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    DateCard(
                        title = it.date,
                        totalSpending = it.cost,
                        modifier = Modifier.padding(bottom = 12.dp),
                    )
                }
            }
        }
    }
    }



