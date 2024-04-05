package com.example.mybudgetapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybudgetapp.R
import com.example.mybudgetapp.data.SpendingCategoryDisplayObject
import com.example.mybudgetapp.ui.navigation.NavigationDestination
import com.example.mybudgetapp.ui.theme.dmSans
import com.example.mybudgetapp.ui.widgets.BudgetTopAppBar
import com.example.mybudgetapp.ui.widgets.ItemCard
import com.example.mybudgetapp.ui.widgets.SpendingProgress
import com.example.mybudgetapp.ui.widgets.TotalIncomeCard

object TotalIncomeDestination: NavigationDestination {
    override val route = "TotalIncome"
    override val titleRes = R.string.total_income_screen
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TotalIncomeScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold (
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { BudgetTopAppBar(
            canNavigateBack = true,
            title = "" ,
            navigateBack = navigateBack,
            scrollBehavior = scrollBehavior
        )}
    ) {paddingValues ->
        TotalIncomeBody(Modifier.padding(paddingValues))
    }
}

@Composable
fun TotalIncomeBody(
    modifier: Modifier = Modifier
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
                    title = R.string.total_income_in,
                    totalSpending = "345"
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .padding(vertical = 2.dp, horizontal = 12.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Your Income",
                        fontFamily = dmSans,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            }
            items(12) {
                ItemCard(
                    title = "R.string.other_spending",
                    totalSpending = "43",
                    modifier = Modifier.padding(bottom = 12.dp),
                    item = SpendingCategoryDisplayObject.items[0],
                    date = "",
                    imagePath = "",
                    displayItem = SpendingCategoryDisplayObject.items[0]
                )
            }
        }
    }
}

