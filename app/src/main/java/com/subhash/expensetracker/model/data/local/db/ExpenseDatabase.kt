package com.subhash.expensetracker.model.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.subhash.expensetracker.model.data.local.converter.Converters
import com.subhash.expensetracker.model.data.local.dao.ExpenseDao
import com.subhash.expensetracker.model.data.local.entity.Expense

@TypeConverters(Converters::class)
@Database(
    entities = [Expense::class],
    version = 1,
    exportSchema = false
)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
}