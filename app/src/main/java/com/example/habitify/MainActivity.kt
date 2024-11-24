package com.example.habitify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.habitify.data.model.local.HabitDB
import com.example.habitify.data.model.local.HabitRepository
import com.example.habitify.ui.theme.HabitifyTheme
import com.example.habitify.viewmodel.HabitViewModel
import com.example.habitify.viewmodel.HabitViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Initialize Database, Repository, and ViewModel
        val database = HabitDB.getDatabase(application)
        val repository = HabitRepository(database.habitDAO())
        val habitViewModel: HabitViewModel by viewModels { HabitViewModelFactory(repository) }

        val authViewModel: AuthViewModel by viewModels()
        setContent {
            HabitifyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(
                        authViewModel = authViewModel,
                        viewModel = habitViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
