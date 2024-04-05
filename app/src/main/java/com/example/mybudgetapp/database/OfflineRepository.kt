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

    override fun getItemWithPurchaseDetails(month: Int, year: Int): Flow<List<ItemWithPurchaseDetails>>
     = itemWithPurchaseDetailsDao.getAllItemsWithPurchaseDetails(month = month, year = year)

    override fun getItemWithPurchaseDetailsForCategory(month: Int, year: Int, category: String)
    = itemWithPurchaseDetailsDao.getAllItemsWithPurchaseDetailsForCategory(month, year, category)

    override fun getTotalSpendingOnCategory(category: String, year: Int, month: Int): Flow<Double>
    = itemWithPurchaseDetailsDao.getTotalSpendingOnCategory(category = category, month = month, year = year)

    override fun getTotalSpendingOverall(year: Int, month: Int): Flow<Double>
    = itemWithPurchaseDetailsDao.getTotalSpendingOverall(year = year, month = month)


}