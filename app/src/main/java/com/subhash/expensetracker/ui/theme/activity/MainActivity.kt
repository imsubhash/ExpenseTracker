package com.subhash.expensetracker.ui.theme.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.subhash.expensetracker.navigation.ExpenseNavigation
import com.subhash.expensetracker.ui.theme.ExpenseTrackerTheme
import com.subhash.expensetracker.viewmodel.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

            ExpenseTrackerTheme(darkTheme = isDarkTheme) {
                Scaffold(modifier = Modifier.Companion.fillMaxSize()) { innerPadding ->
                    ExpenseNavigation(
                        modifier = Modifier.Companion.padding(innerPadding),
                        themeViewModel = themeViewModel
                    )
                }
            }
        }
    }
}