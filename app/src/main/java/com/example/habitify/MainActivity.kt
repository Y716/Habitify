package com.example.habitify

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.habitify.data.model.local.HabitDB
import com.example.habitify.data.model.local.HabitRepository
import com.example.habitify.ui.components.BottomAppBar
import com.example.habitify.ui.theme.HabitifyTheme
import com.example.habitify.viewmodel.HabitViewModel
import com.example.habitify.viewmodel.HabitViewModelFactory
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Database, Repository, and ViewModel
        val database = HabitDB.getDatabase(application)
        val repository = HabitRepository(database.habitDAO())
        val habitViewModel: HabitViewModel by viewModels { HabitViewModelFactory(repository) }
        val authViewModel: AuthViewModel by viewModels()

        FirebaseApp.initializeApp(this)

        setContent {
            HabitifyTheme {
                val navController: NavHostController = rememberNavController() // Create NavController

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                            BottomAppBar(navController = navController)
                    },
                    content = { innerPadding ->
                        // Pass NavController to AppNavigation
                        AppNavigation(
                            modifier = Modifier.padding(innerPadding),
                            authViewModel = authViewModel,
                            viewModel = habitViewModel,
                            navController = navController
                        )
                    }
                )

            }
        }
    }
}

