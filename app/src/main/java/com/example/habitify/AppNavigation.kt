package com.example.habitify

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.habitify.pages.AddHabitPage
import com.example.habitify.pages.HomePage
import com.example.habitify.pages.LoginPage
import com.example.habitify.pages.PomodoroPage
import com.example.habitify.pages.RegisterPage
import com.example.habitify.pages.StatisticsPage
//import com.example.habitify.pages.StatisticPage
import com.example.habitify.viewmodel.HabitViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    viewModel: HabitViewModel,
    navController: NavHostController
) {
//    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "homepage",
        modifier = modifier
    ) {
        composable("login") {
            LoginPage(modifier, navController, authViewModel)
        }
        composable("signup") {
            RegisterPage(modifier, navController, authViewModel)
        }
        composable("homepage") {
            HomePage(modifier, navController, authViewModel, viewModel)
        }
        composable(
            route = "add_habit/{selectedDate}",
            arguments = listOf(navArgument("selectedDate") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val selectedDate = backStackEntry.arguments?.getString("selectedDate") ?: LocalDate.now().toString()
            AddHabitPage(navController, viewModel, selectedDate)
        }
        composable("statistics_page") {
            StatisticsPage(viewModel, navController)
        }
        composable("pomodoro_clock") {
            PomodoroPage()
        }
        composable("profile") {
//            ProfilePage() // Placeholder for a profile or logout page
        }
    }
}
