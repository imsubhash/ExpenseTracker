package com.subhash.expensetracker.ui.theme.state

data class ExpenseUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)
