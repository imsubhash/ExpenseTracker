package com.subhash.expensetracker.navigation

import androidx.compose.runtime.Composable
import com.subhash.expensetracker.viewmodel.ThemeViewModel

import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.subhash.expensetracker.ui.theme.screen.ExpenseEntryScreen
import com.subhash.expensetracker.ui.theme.screen.ExpenseListScreen
import com.subhash.expensetracker.ui.theme.screen.ExpenseReportScreen
import com.subhash.expensetracker.viewmodel.ExpenseViewModel

@Composable
fun ExpenseNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    themeViewModel: ThemeViewModel
) {
    val expenseViewModel: ExpenseViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = "expense_entry",
        modifier = modifier
    ) {
        composable("expense_entry") {
            ExpenseEntryScreen(
                viewModel = expenseViewModel,
                themeViewModel = themeViewModel,
                onNavigateToList = { navController.navigate("expense_list") },
                onNavigateToReports = { navController.navigate("expense_reports") }
            )
        }
        composable("expense_list") {
            ExpenseListScreen(
                viewModel = expenseViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEntry = { navController.navigate("expense_entry") },
                onNavigateToReports = { navController.navigate("expense_reports") }
            )
        }
        composable("expense_reports") {
            ExpenseReportScreen(
                viewModel = expenseViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEntry = { navController.navigate("expense_entry") },
                onNavigateToList = { navController.navigate("expense_list") }
            )
        }
    }
}