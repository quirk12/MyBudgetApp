package com.example.mybudgetapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.compose.MyBudgetAppTheme
import com.example.mybudgetapp.ui.screens.AddingItem
import com.example.mybudgetapp.ui.screens.AddingItemBody
import com.example.mybudgetapp.ui.screens.SpendingOnCategoryBody
import com.example.mybudgetapp.ui.screens.SpendingOnCategoryScreen
import com.example.mybudgetapp.ui.screens.ThisMonthScreen
import com.example.mybudgetapp.ui.screens.TotalIncomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyBudgetAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyBudgetApp()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyBudgetAppTheme {
        Greeting("Android")
    }
}