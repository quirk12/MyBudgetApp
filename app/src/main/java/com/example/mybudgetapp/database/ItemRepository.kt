package com.example.mybudgetapp.database

import kotlinx.coroutines.flow.Flow

interface ItemRepository {

    suspend fun insertItem(item: Item)
    suspend fun updateItem(item: Item)
    suspend fun updateItemDateWithId(date:String, id: Long)
    suspend fun deleteItem(item: Item)
    suspend fun getItemIdFromName(name:String): Long

    suspend fun updateItemImagePathWithId(imagePath:String, id: Long)

    suspend fun insetPurchaseDetails(purchaseDetails: PurchaseDetails)
    suspend fun updatePurchaseDetails(purchaseDetails: PurchaseDetails)
    suspend fun deletePurchaseDetails(purchaseDetails: PurchaseDetails)
    fun getAllMonths(year: Int): Flow<List<Int>>

    fun getTotalSpendingOnCategory(category: String, year: Int, month: Int): Flow<Double>
    fun getTotalSpendingOverall(year: Int, month: Int): Flow<Double>

    fun getItemWithPurchaseDetails(month: Int, year:Int): Flow<List<ItemWithPurchaseDetails>>
    fun getItemWithPurchaseDetailsForCategory(month: Int, year:Int, category: String): Flow<List<ItemWithPurchaseDetails>>
}