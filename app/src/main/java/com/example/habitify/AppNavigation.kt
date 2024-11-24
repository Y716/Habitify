package com.example.habitify

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.habitify.pages.AddHabitPage
import com.example.habitify.pages.HomePage
import com.example.habitify.pages.LoginPage
import com.example.habitify.pages.RegisterPage
import com.example.habitify.pages.StatisticsPage
//import com.example.habitify.pages.StatisticPage
import com.example.habitify.viewmodel.HabitViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel, viewModel: HabitViewModel){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login" , builder = {
        composable("login"){
            LoginPage(modifier, navController, authViewModel)
        }
        composable("signup"){
            RegisterPage(modifier, navController, authViewModel)
        }
        composable("homepage"){
            HomePage(modifier, navController, authViewModel, viewModel)
        }
        composable(
            route = "add_habit/{selectedDate}", // Add selectedDate as a navigation argument
            arguments = listOf(navArgument("selectedDate") {
                type = NavType.StringType // Expect a String for the selected date
            })
        ) { backStackEntry ->
            val selectedDate = backStackEntry.arguments?.getString("selectedDate")
                ?: LocalDate.now().toString() // Default to today's date if null
            AddHabitPage(navController = navController, viewModel = viewModel, selectedDate = selectedDate)
        }
        composable("statistics_page") {
//            StatisticPage(navController, viewModel)
            StatisticsPage(viewModel, navController)
        }
        composable("pomodoro_clock"){
            HomePage(modifier, navController, authViewModel, viewModel)
        }

    })
}