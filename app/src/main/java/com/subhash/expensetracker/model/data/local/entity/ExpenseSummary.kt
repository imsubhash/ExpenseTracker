package com.subhash.expensetracker.model.data.local.entity

data class ExpenseSummary(
    val totalAmount: Double,
    val totalCount: Int,
    val categoryBreakdown: Map<ExpenseCategory, Double>,
    val dailyBreakdown: Map<String, Double>
)
