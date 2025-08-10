package com.subhash.expensetracker.model.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.subhash.expensetracker.model.data.local.entity.Expense
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY date DESC, createdAt DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getExpensesByDateRange(startDate: Date, endDate: Date): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE DATE(date/1000, 'unixepoch') = DATE(:date/1000, 'unixepoch') ORDER BY createdAt DESC")
    fun getExpensesByDate(date: Date): Flow<List<Expense>>

    @Insert
    suspend fun insertExpense(expense: Expense): Long

//    @Delete
//    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT SUM(amount) FROM expenses WHERE DATE(date/1000, 'unixepoch') = DATE(:date/1000, 'unixepoch')")
    suspend fun getTotalForDate(date: Date): Double?
}
