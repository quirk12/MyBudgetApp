package com.example.mybudgetapp.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "budget_item", indices = [Index(value = ["name"], unique = true)])
data class Item(
    @PrimaryKey(autoGenerate = true)
    val itemId: Long = 0,
    val name:String,
    val date: String,
    val category: String,
    val picturePath: String? = null
)

@Entity(
    tableName = "purchase_details",
    foreignKeys = [ForeignKey(
        entity = Item::class,
        parentColumns = ["itemId"],
        childColumns = ["itemId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class PurchaseDetails(
    @PrimaryKey(autoGenerate = true)
    val purchaseId: Long = 0,
    @ColumnInfo(index = true)
    val itemId:Long,
    val cost: Double,
    val purchaseDate: String,
    val month: Int,
    val year: Int,
)

data class ItemWithPurchaseDetails(
    @Embedded val item: Item,
    val totalCost: Double,
)


