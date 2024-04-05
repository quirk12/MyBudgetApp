package com.example.mybudgetapp.database

import android.content.Context

interface AppContainer {
    val itemRepository: ItemRepository
}

class AppDataContainer(context: Context): AppContainer {

    override val itemRepository: ItemRepository by lazy {
        OfflineRepository(
            itemDao = BudgetDatabase.getDatabase(context).itemDao(),
            purchaseDetailsDao = BudgetDatabase.getDatabase(context).purchaseDetailsDao(),
            itemWithPurchaseDetailsDao = BudgetDatabase.getDatabase(context).itemWithPurchaseDetailsDao()
        )
    }

}