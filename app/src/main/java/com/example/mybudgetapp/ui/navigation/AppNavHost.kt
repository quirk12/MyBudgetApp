package com.example.mybudgetapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mybudgetapp.ui.screens.AddingItem
import com.example.mybudgetapp.ui.screens.AddingItemDestination
import com.example.mybudgetapp.ui.screens.SpendingOnCategoryDestination
import com.example.mybudgetapp.ui.screens.SpendingOnCategoryScreen
import com.example.mybudgetapp.ui.screens.ThisMonthDestination
import com.example.mybudgetapp.ui.screens.ThisMonthScreen
import com.example.mybudgetapp.ui.screens.TotalIncomeDestination
import com.example.mybudgetapp.ui.screens.TotalIncomeScreen

@Composable
fun AppNavHost (
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController ,
        startDestination = ThisMonthDestination.route,
        modifier = modifier
    ) {
        composable(ThisMonthDestination.route) {
            ThisMonthScreen(
                navigateToSpendingOnCategory = { category, month ->
                    navController.navigate(

                        "${SpendingOnCategoryDestination.route}/$category/$month"
                    )
                                               },
                navigateToTotalIncome = {navController.navigate(TotalIncomeDestination.route)}
            )
        }
        composable(AddingItemDestination.route) {
            AddingItem(
                navigateBack = {navController.navigateUp()}
            )
        }
        composable(
            route = SpendingOnCategoryDestination.routeWithArgs,
            arguments = listOf(
                navArgument(SpendingOnCategoryDestination.category) {
                    type = NavType.StringType
                },
                navArgument(SpendingOnCategoryDestination.month) {
                    type = NavType.StringType
                }
            )
        ) {
            SpendingOnCategoryScreen(
                navigateToAddItem = {navController.navigate(AddingItemDestination.route)},
                navigateBack = {navController.navigateUp()}
            )
        }
        composable(TotalIncomeDestination.route) {
            TotalIncomeScreen(
                navigateBack = {navController.popBackStack()}
            )
        }
    }
}