package com.subhash.expensetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.subhash.expensetracker.model.data.local.entity.Expense
import com.subhash.expensetracker.model.data.local.entity.ExpenseCategory
import com.subhash.expensetracker.model.data.local.entity.ExpenseSummary
import com.subhash.expensetracker.model.data.repository.ExpenseRepository
import com.subhash.expensetracker.ui.theme.state.ExpenseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val repository: ExpenseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpenseUiState())
    val uiState: StateFlow<ExpenseUiState> = _uiState.asStateFlow()

    private val _todayTotal = MutableStateFlow(0.0)
    val todayTotal: StateFlow<Double> = _todayTotal.asStateFlow()

    private val _selectedDate = MutableStateFlow(Date())
    val selectedDate: StateFlow<Date> = _selectedDate.asStateFlow()

    val todayExpenses: Flow<List<Expense>> = repository.getExpensesByDate(Date())

    val selectedDateExpenses: Flow<List<Expense>> = _selectedDate.flatMapLatest { date ->
        repository.getExpensesByDate(date)
    }

    init {
        loadTodayTotal()
        observeTodayExpenses()
    }

    private fun observeTodayExpenses() {
        viewModelScope.launch {
            todayExpenses.collect { expenses ->
                _todayTotal.value = expenses.sumOf { it.amount }
            }
        }
    }

    private fun loadTodayTotal() {
        viewModelScope.launch {
            _todayTotal.value = repository.getTotalForToday()
        }
    }

    fun addExpense(
        title: String,
        amount: String,
        category: ExpenseCategory,
        notes: String,
        imagePath: String? = null
    ) {
        viewModelScope.launch {
            try {
                val amountDouble = amount.toDoubleOrNull()
                if (amountDouble == null || amountDouble <= 0) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Please enter a valid amount"
                    )
                    return@launch
                }

                if (title.isBlank()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Please enter a title"
                    )
                    return@launch
                }

                _uiState.value = _uiState.value.copy(isLoading = true, error = null)

                val expense = Expense(
                    title = title.trim(),
                    amount = amountDouble,
                    category = category,
                    notes = notes.trim().take(100), // Max 100 chars
                    receiptImagePath = imagePath,
                    date = Date()
                )

                val isDuplicate = repository.checkForDuplicate(expense)
                if (isDuplicate) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Similar expense already exists for today"
                    )
                    return@launch
                }

                repository.insertExpense(expense)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    error = null
                )
                loadTodayTotal()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to add expense: ${e.message}"
                )
            }
        }
    }

    fun setSelectedDate(date: Date) {
        _selectedDate.value = date
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(isSuccess = false)
    }

    fun getLast7DaysExpenses(): Flow<List<Expense>> {
        val calendar = Calendar.getInstance()
        val endDate = calendar.time
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val startDate = calendar.time

        return repository.getExpensesByDateRange(startDate, endDate)
    }

    fun getExpenseSummary(expenses: List<Expense>): ExpenseSummary {
        return repository.getExpenseSummary(expenses)
    }
}