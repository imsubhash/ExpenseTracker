package com.subhash.expensetracker.model.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val category: ExpenseCategory,
    val notes: String = "",
    val receiptImagePath: String? = null,
    val date: Date = Date(),
    val createdAt: Long = System.currentTimeMillis()
)

enum class ExpenseCategory(val displayName: String) {
    STAFF("Staff"),
    TRAVEL("Travel"),
    FOOD("Food"),
    UTILITY("Utility")
}