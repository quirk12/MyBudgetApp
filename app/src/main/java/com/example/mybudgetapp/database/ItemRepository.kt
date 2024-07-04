package com.example.mybudgetapp.database

import kotlinx.coroutines.flow.Flow

interface ItemRepository {

    suspend fun insertItem(item: Item)
    suspend fun updateItem(item: Item)
    suspend fun updateItemDateWithId(date:String, id: Long)
    suspend fun deleteItem(item: Item)
    suspend fun getItemIdFromName(name:String): Long

    fun getItemFromId(id: Long): Flow<Item>

    suspend fun deleteItemWithId(id: Long)

    suspend fun updateItemImagePathWithId(imagePath:String, id: Long)

    suspend fun insetPurchaseDetails(purchaseDetails: PurchaseDetails)
    suspend fun updatePurchaseDetails(purchaseDetails: PurchaseDetails)
    suspend fun deletePurchaseDetails(purchaseDetails: PurchaseDetails)
    fun getAllMonths(year: Int): Flow<List<Int>>
    fun getAllYears(): Flow<List<Int>>

    fun getTotalSpendingOnCategory(category: String, year: Int, month: Int): Flow<Double>
    fun getTotalSpendingOverall(year: Int, month: Int): Flow<Double>
    fun getTotalIncomeOverall(year: Int, month: Int): Flow<Double>

    fun getTotalSpendingOnCategoryForYear(category: String, year: Int): Flow<Double>
    fun getTotalSpendingOverallForYear(year: Int): Flow<Double>
    fun getTotalIncomeOverallForYear(year: Int): Flow<Double>

    fun getItemWithPurchaseDetails(month: Int, year:Int): Flow<List<ItemWithPurchaseDetails>>

    fun getItemWithPurchaseDetailsForYear(year:Int): Flow<List<ItemWithPurchaseDetails>>
    fun getItemWithPurchaseDetailsForCategory(month: Int, year:Int, category: String): Flow<List<ItemWithPurchaseDetails>>
    fun getItemWithPurchaseDetailsForCategoryForYear(year:Int, category: String): Flow<List<ItemWithPurchaseDetails>>
    fun getItemDates(id: Long): Flow<List<PurchaseDetails>>
}