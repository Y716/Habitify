package com.example.habitify

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.habitify.pages.HomePage
import com.example.habitify.pages.LoginPage
import com.example.habitify.pages.RegisterPage

@Composable
fun AppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login" , builder = {
        composable("Login"){
            LoginPage(modifier, navController, authViewModel)
        }
        composable("signup"){
            RegisterPage(modifier, navController, authViewModel)
        }
        composable("homepage"){
            HomePage(modifier, navController, authViewModel)
        }
    })
}