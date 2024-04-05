package com.example.mybudgetapp.ui.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mybudgetapp.R
import com.example.mybudgetapp.data.SpendingCategoryDisplayObject
import com.example.mybudgetapp.ui.navigation.NavigationDestination
import com.example.mybudgetapp.ui.theme.dmSans
import com.example.mybudgetapp.ui.theme.inter
import com.example.mybudgetapp.ui.viewmodels.AppViewModelProvider
import com.example.mybudgetapp.ui.viewmodels.ScreenItemsUiState
import com.example.mybudgetapp.ui.viewmodels.ThisMonthScreenUiState
import com.example.mybudgetapp.ui.viewmodels.ThisMonthScreenViewModel
import com.example.mybudgetapp.ui.widgets.BottomNavigationBar
import com.example.mybudgetapp.ui.widgets.BudgetTopAppBar
import com.example.mybudgetapp.ui.widgets.CategoryCard
import com.example.mybudgetapp.ui.widgets.DropDownMenu
import com.example.mybudgetapp.ui.widgets.TotalIncomeSpending

object ThisMonthDestination: NavigationDestination {
    override val route = "ThisMonthScreen"
    override val titleRes = R.string.this_month_screen

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThisMonthScreen(
    navigateToSpendingOnCategory: (String, String) -> Unit,
    navigateToTotalIncome: () -> Unit,
){
    val viewModel: ThisMonthScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val uiState = viewModel.uiState.collectAsState()
    Scaffold (
        bottomBar = { BottomNavigationBar()}
    ) {innerPadding ->

        ThisMonthScreenBody(
            modifier = Modifier.padding(innerPadding),
            uiState = uiState.value,
            updateExpandState = viewModel::updateVisibility,
            updateMonth = {viewModel.updateSelectedMonth(it)},
            navigateToSpendingOnCategory = navigateToSpendingOnCategory,
            navigateToTotalIncome = navigateToTotalIncome
        )

    }
}

@Composable
fun ThisMonthScreenBody(
    modifier: Modifier = Modifier,
    updateExpandState: (ScreenItemsUiState) -> Unit,
    uiState: ThisMonthScreenUiState,
    navigateToSpendingOnCategory: (String, String) -> Unit,
    navigateToTotalIncome: () -> Unit,
    updateMonth: (Int) -> Unit,
){
    val density = LocalDensity.current

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(vertical = 32.dp)
                .fillMaxWidth().onSizeChanged {
                    updateExpandState(
                        uiState.screenItemsUiState.value.copy(
                            itemHeight = with(density) {it.height.toDp()}
                        )
                    )

                }
        ) {
            Text(
                text = uiState.currentMonth,
                fontFamily = dmSans,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp
            )
            IconButton(
                onClick = {
                          updateExpandState(
                              uiState.screenItemsUiState.value.copy(
                                  isDropDownMenuVisible = !uiState.screenItemsUiState.value.isDropDownMenuVisible
                              ),
                          )
                },
                modifier = Modifier.padding(horizontal = 4.dp).pointerInput(true) {
                    detectTapGestures(
                        onPress = {
                            updateExpandState(
                                uiState.screenItemsUiState.value.copy(
                                    offSet = DpOffset(it.x.toDp(), it.y.toDp())
                                )
                            )
                        }
                    )
                }
            ) {
                Icon(
                    imageVector = if (uiState.screenItemsUiState.value.isDropDownMenuVisible) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown ,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
                DropDownMenu(
                    dropDownItem = uiState.months ,
                    onDismissRequest = updateExpandState,
                    isContextMenuVisible = uiState.screenItemsUiState.value.isDropDownMenuVisible,
                    uiState = uiState.screenItemsUiState.value,
                    onSelected = { index ->
                        updateExpandState(
                            uiState.screenItemsUiState.value.copy(
                                isDropDownMenuVisible = !uiState.screenItemsUiState.value.isDropDownMenuVisible
                            )
                        )
                        updateMonth(index)

                    }
                )

            }


        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            TotalIncomeSpending(
                icon = R.drawable.baseline_attach_money_24,
                incomeOrSpending = R.string.income,
                total = uiState.totalSpending,
                modifier = Modifier.weight(1f),
                navigateToTotalIncome = navigateToTotalIncome

            )
            Spacer(modifier = Modifier.width(16.dp))
            TotalIncomeSpending(
                icon = R.drawable.baseline_money_off_24,
                incomeOrSpending = R.string.spending,
                total = uiState.totalIncome,
                modifier = Modifier.weight(1f),
                navigateToTotalIncome = navigateToTotalIncome

            )
        }

        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(all = 8.dp)
                .fillMaxSize()
        ) {
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
            CategoryCard(
                item = SpendingCategoryDisplayObject.items[0],
                totalSpending = uiState.totalSpendingOnFood,
                modifier = Modifier.padding(bottom = 16.dp),
                navigateToSpendingOnCategory = {navigateToSpendingOnCategory(
                    "food",
                    uiState.currentMonth
                )}
            )
            CategoryCard(
                item = SpendingCategoryDisplayObject.items[1],
                totalSpending = uiState.totalSpendingOnTransportation,
                modifier = Modifier.padding(bottom = 16.dp),
                navigateToSpendingOnCategory = {
                    navigateToSpendingOnCategory(
                        "transportation",
                        uiState.currentMonth
                    )
                }
            )
            CategoryCard(
                item = SpendingCategoryDisplayObject.items[2],
                totalSpending = uiState.totalSpendingOnOthers,
                modifier = Modifier.padding(bottom = 16.dp),
                navigateToSpendingOnCategory = {
                    navigateToSpendingOnCategory(
                        "others",
                        uiState.currentMonth
                    )
                }
            )
        }
    }

}

