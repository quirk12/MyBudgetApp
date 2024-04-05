package com.example.mybudgetapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertItem(item: Item)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateItem(item: Item)

    @Delete
    suspend fun deleteItem(item: Item)

    @Query("Select itemId from budget_item where LOWER(name) = LOWER(:name) ")
    suspend fun getIdFromName(name: String): Long

    @Query("""
        update budget_item
        set date = :date
        where itemId = :id
        """)
    suspend fun updateItemDateWithId(date:String, id: Long)

    @Query("""
        update budget_item
        set picturePath = :imagePath
        where itemId = :id
        """)
    suspend fun updateItemImagePathWithId(imagePath:String, id: Long)

}

@Dao
interface PurchaseDetailsDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertItem(purchaseDetails: PurchaseDetails)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateItem(purchaseDetails: PurchaseDetails)

    @Delete
    suspend fun deleteItem(purchaseDetails: PurchaseDetails)


    @Query("""
        select distinct month from purchase_details
         where year = :year
         order by month asc
    """)
    fun getAllMonths(year: Int): Flow<List<Int>>



}

@Dao
interface ItemWithPurchaseDetailsDao {
    @Query("""
        select i.itemId, i.name, i.category, i.picturePath, i.date, sum(p.cost) as totalCost
        from budget_item as i
        left join purchase_details as p
        on i.itemId = p.itemId
        where p.month = :month and p.year = :year
        group by i.itemId, i.name, i.category, i.picturePath, i.date
    """)
    fun getAllItemsWithPurchaseDetails(month: Int, year: Int): Flow<List<ItemWithPurchaseDetails>>
    @Query("""
        select i.itemId, i.name, i.category, i.picturePath, i.date, sum(p.cost) as totalCost
        from budget_item as i
        left join purchase_details as p
        on i.itemId = p.itemId
        where p.month = :month and p.year = :year and i.category = :category
        group by i.itemId, i.name, i.category, i.picturePath, i.date
    """)
    fun getAllItemsWithPurchaseDetailsForCategory(month: Int, year: Int, category: String): Flow<List<ItemWithPurchaseDetails>>

    @Query("""
        select sum(p.cost) from purchase_details as p
        join budget_item as i 
        on i.itemId = p.itemId
        where i.category = :category and p.year = :year and p.month = :month
    """)
    fun getTotalSpendingOnCategory(category: String, year: Int, month: Int): Flow<Double>

    @Query("""
        select sum(p.cost) from purchase_details as p
        join budget_item as i 
        on i.itemId = p.itemId
        where  p.year = :year and p.month = :month
    """)
    fun getTotalSpendingOverall(year: Int, month: Int): Flow<Double>
}