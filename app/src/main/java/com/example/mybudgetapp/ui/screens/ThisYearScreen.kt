package com.example.mybudgetapp.ui.screens

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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mybudgetapp.R
import com.example.mybudgetapp.data.SpendingCategoryDisplayObject
import com.example.mybudgetapp.ui.navigation.NavigationDestination
import com.example.mybudgetapp.ui.theme.dmSans
import com.example.mybudgetapp.ui.viewmodels.AppViewModelProvider
import com.example.mybudgetapp.ui.viewmodels.ScreenItemsUiState
import com.example.mybudgetapp.ui.viewmodels.ThisYearScreenUiState
import com.example.mybudgetapp.ui.viewmodels.ThisYearScreenViewModel
import com.example.mybudgetapp.ui.widgets.BottomNavigationBar
import com.example.mybudgetapp.ui.widgets.CategoryCard
import com.example.mybudgetapp.ui.widgets.DropDownMenu
import com.example.mybudgetapp.ui.widgets.TotalIncomeSpending

object ThisYearDestination: NavigationDestination {
    override val route = "ThisYearScreen"
    override val titleRes = R.string.this_month_screen

}

@Composable
fun ThisYearScreen(
    navigateToSpendingOnCategoryForYear: (String, Int) -> Unit,
    navigateToTotalIncomeForYear: (Int, Boolean) -> Unit,
    navigateToThisMonthScreen: () -> Unit ,
    navigateToThisYearScreen: () -> Unit
){
    val viewModel: ThisYearScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val uiState = viewModel.uiState.collectAsState()
    Scaffold (
        bottomBar = {
            BottomNavigationBar(
                navigateToThisMonthScreen = navigateToThisMonthScreen,
                navigateToThisYearScreen = navigateToThisYearScreen,
                selectedItemIndex = 0
            )
        }
    ) {innerPadding ->

        ThisYearScreenBody(
            modifier = Modifier.padding(innerPadding),
            uiState = uiState.value,
            updateExpandState = viewModel::updateVisibility,
            updateYear = {viewModel.updateSelectedMonth(it)},
            navigateToSpendingOnCategoryForYear = navigateToSpendingOnCategoryForYear,
            navigateToTotalIncomeForYear = navigateToTotalIncomeForYear
        )

    }
}

@Composable
fun ThisYearScreenBody(
    modifier: Modifier = Modifier,
    updateExpandState: (ScreenItemsUiState) -> Unit,
    uiState: ThisYearScreenUiState,
    navigateToSpendingOnCategoryForYear: (String, Int) -> Unit,
    navigateToTotalIncomeForYear: (Int, Boolean) -> Unit,
    updateYear: (Int) -> Unit,
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
                .fillMaxWidth()
                .onSizeChanged {
                    updateExpandState(
                        uiState.screenItemsUiState.value.copy(
                            itemHeight = with(density) { it.height.toDp() }
                        )
                    )

                }
        ) {
            Text(
                text = uiState.currentYear,
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
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .pointerInput(true) {
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
                    dropDownItem = uiState.years ,
                    onDismissRequest = updateExpandState,
                    isContextMenuVisible = uiState.screenItemsUiState.value.isDropDownMenuVisible,
                    uiState = uiState.screenItemsUiState.value,
                    onSelected = { index ->
                        updateExpandState(
                            uiState.screenItemsUiState.value.copy(
                                isDropDownMenuVisible = !uiState.screenItemsUiState.value.isDropDownMenuVisible
                            )
                        )
                        updateYear(index)

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
                total = uiState.totalIncomeForYear,
                modifier = Modifier.weight(1f),
                navigateToTotalIncome = {
                    navigateToTotalIncomeForYear(
                        uiState.currentYear.toInt(),
                        true
                    )
                }

            )
            Spacer(modifier = Modifier.width(16.dp))
            TotalIncomeSpending(
                icon = R.drawable.baseline_money_off_24,
                incomeOrSpending = R.string.spending,
                total = uiState.totalSpendingForYear,
                modifier = Modifier.weight(1f),
                navigateToTotalIncome = {
                    navigateToTotalIncomeForYear(
                        uiState.currentYear.toInt(),
                        false
                    )
                }

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
                totalSpending = uiState.totalSpendingOnFoodForYear,
                modifier = Modifier.padding(bottom = 16.dp),
                navigateToSpendingOnCategory = {navigateToSpendingOnCategoryForYear(
                    "food",
                    uiState.currentYear.toInt()
                )}
            )
            CategoryCard(
                item = SpendingCategoryDisplayObject.items[1],
                totalSpending = uiState.totalSpendingOnTransportationForYear,
                modifier = Modifier.padding(bottom = 16.dp),
                navigateToSpendingOnCategory = {
                    navigateToSpendingOnCategoryForYear(
                        "transportation",
                        uiState.currentYear.toInt()
                    )
                }
            )
            CategoryCard(
                item = SpendingCategoryDisplayObject.items[2],
                totalSpending = uiState.totalSpendingOnOthersForYear,
                modifier = Modifier.padding(bottom = 16.dp),
                navigateToSpendingOnCategory = {
                    navigateToSpendingOnCategoryForYear(
                        "others",
                        uiState.currentYear.toInt()
                    )
                }
            )
        }
    }

}

