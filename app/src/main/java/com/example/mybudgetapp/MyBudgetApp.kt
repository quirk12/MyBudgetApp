package com.example.mybudgetapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mybudgetapp.ui.navigation.AppNavHost

@Composable
fun MyBudgetApp (
    navController: NavHostController = rememberNavController()
) {
    AppNavHost(navController = navController)
}