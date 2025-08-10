package com.subhash.expensetracker.model.data.repository

import com.subhash.expensetracker.model.data.local.dao.ExpenseDao
import com.subhash.expensetracker.model.data.local.entity.Expense
import com.subhash.expensetracker.model.data.local.entity.ExpenseSummary
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao
) {
    fun getAllExpenses(): Flow<List<Expense>> = expenseDao.getAllExpenses()

    fun getExpensesByDate(date: Date): Flow<List<Expense>> = expenseDao.getExpensesByDate(date)

    fun getExpensesByDateRange(startDate: Date, endDate: Date): Flow<List<Expense>> =
        expenseDao.getExpensesByDateRange(startDate, endDate)

    suspend fun insertExpense(expense: Expense): Long = expenseDao.insertExpense(expense)

//    suspend fun deleteExpense(expense: Expense) = expenseDao.deleteExpense(expense)

    suspend fun getTotalForToday(): Double {
        return expenseDao.getTotalForDate(Date()) ?: 0.0
    }

    fun getExpenseSummary(expenses: List<Expense>): ExpenseSummary {
        val totalAmount = expenses.sumOf { it.amount }
        val totalCount = expenses.size

        val categoryBreakdown = expenses.groupBy { it.category }
            .mapValues { (_, expenseList) -> expenseList.sumOf { it.amount } }

        val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
        val dailyBreakdown = expenses.groupBy { dateFormat.format(it.date) }
            .mapValues { (_, expenseList) -> expenseList.sumOf { it.amount } }

        return ExpenseSummary(totalAmount, totalCount, categoryBreakdown, dailyBreakdown)
    }

    suspend fun checkForDuplicate(expense: Expense): Boolean {
        // Simple duplicate detection based on title, amount, and same day
        val todayExpenses = expenseDao.getExpensesByDate(expense.date)
        // This is a simplified check - in production, you might want more sophisticated logic
        return false // For now, we'll allow all expenses
    }
}