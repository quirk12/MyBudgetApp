package com.example.mybudgetapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase



@Database(entities = [Item::class, PurchaseDetails::class], version = 3, exportSchema = false)
abstract class BudgetDatabase: RoomDatabase() {

    abstract fun itemDao(): ItemDao
    abstract fun purchaseDetailsDao(): PurchaseDetailsDao
    abstract fun itemWithPurchaseDetailsDao(): ItemWithPurchaseDetailsDao

    companion object {
        @Volatile
        private var instance: BudgetDatabase? = null

        fun getDatabase(context: Context): BudgetDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(context, BudgetDatabase::class.java, "test_database")
                    .fallbackToDestructiveMigration()
                    .build().also { instance = it }
            }
        }
    }

}