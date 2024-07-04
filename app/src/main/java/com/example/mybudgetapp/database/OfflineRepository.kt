package com.example.mybudgetapp.database

import kotlinx.coroutines.flow.Flow

class OfflineRepository (
    private val itemDao: ItemDao,
    private val purchaseDetailsDao: PurchaseDetailsDao,
    private val itemWithPurchaseDetailsDao: ItemWithPurchaseDetailsDao
): ItemRepository {


    override suspend fun insertItem(item: Item) = itemDao.insertItem(item)
    override suspend fun deleteItem(item: Item) = itemDao.deleteItem(item)
    override suspend fun updateItem(item: Item) = itemDao.updateItem(item)

    override suspend fun updateItemDateWithId(date: String, id: Long) = itemDao.updateItemDateWithId(date, id)

    override fun getItemFromId(id: Long): Flow<Item>
    = itemDao.getItemFromId(id)
    override suspend fun deleteItemWithId(id: Long)  =
        itemDao.deleteItemWithId(id)

    override suspend fun updateItemImagePathWithId(imagePath: String, id: Long) = itemDao.updateItemImagePathWithId(
        imagePath = imagePath, id = id
    )

    override suspend fun getItemIdFromName(name: String): Long = itemDao.getIdFromName(name)


    override suspend fun insetPurchaseDetails(purchaseDetails: PurchaseDetails)
    = purchaseDetailsDao.insertItem(purchaseDetails)


    override suspend fun updatePurchaseDetails(purchaseDetails: PurchaseDetails)
        = purchaseDetailsDao.updateItem(purchaseDetails)

    override suspend fun deletePurchaseDetails(purchaseDetails: PurchaseDetails)
        = purchaseDetailsDao.deleteItem(purchaseDetails)

    override fun getAllMonths(year: Int) = purchaseDetailsDao.getAllMonths(year)
    override fun getAllYears(): Flow<List<Int>> = purchaseDetailsDao.getAllYears()

    override fun getItemWithPurchaseDetails(month: Int, year: Int): Flow<List<ItemWithPurchaseDetails>>
     = itemWithPurchaseDetailsDao.getAllItemsWithPurchaseDetails(month = month, year = year)

    override fun getItemWithPurchaseDetailsForYear(year: Int): Flow<List<ItemWithPurchaseDetails>>
    = itemWithPurchaseDetailsDao.getAllItemsWithPurchaseDetailsForYear(year)

    override fun getItemWithPurchaseDetailsForCategoryForYear(
        year: Int,
        category: String
    ): Flow<List<ItemWithPurchaseDetails>> =
        itemWithPurchaseDetailsDao.getAllItemsWithPurchaseDetailsForCategoryForYear(year, category)

    override fun getItemWithPurchaseDetailsForCategory(month: Int, year: Int, category: String)
    = itemWithPurchaseDetailsDao.getAllItemsWithPurchaseDetailsForCategory(month, year, category)

    override fun getTotalSpendingOnCategory(category: String, year: Int, month: Int): Flow<Double>
    = itemWithPurchaseDetailsDao.getTotalSpendingOnCategory(category = category, month = month, year = year)

    override fun getTotalSpendingOverall(year: Int, month: Int): Flow<Double>
    = itemWithPurchaseDetailsDao.getTotalSpendingOverall(year = year, month = month)

    override fun getTotalIncomeOverall(year: Int, month: Int): Flow<Double> =
        itemWithPurchaseDetailsDao.getTotalIncomeOverall(year, month = month)

    override fun getTotalIncomeOverallForYear(year: Int): Flow<Double> =
        itemWithPurchaseDetailsDao.getTotalIncomeOverallForYear(year)

    override fun getTotalSpendingOnCategoryForYear(category: String, year: Int): Flow<Double> =
        itemWithPurchaseDetailsDao.getTotalSpendingOnCategoryForYear(category, year)

    override fun getTotalSpendingOverallForYear(year: Int): Flow<Double> =
        itemWithPurchaseDetailsDao.getTotalSpendingOverallForYear(year)
    override fun getItemDates(id: Long): Flow<List<PurchaseDetails>> =
        itemWithPurchaseDetailsDao.getALlDatesForAnItem(id)


}