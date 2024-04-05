package com.example.mybudgetapp

import android.app.Application
import com.example.mybudgetapp.database.AppContainer
import com.example.mybudgetapp.database.AppDataContainer

class BudgetApp: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}