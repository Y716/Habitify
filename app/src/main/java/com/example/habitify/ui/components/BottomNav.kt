package com.example.habitify.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNav(navController: NavController) {
    // Observe the current back stack to get the current route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Define the navigation items for the bottom navigation
    val navigationItems = listOf(
//        NavigationItem(
//            route = "homepage",
//            label = "Home",
//            icon = Icons.Default.Home
//        ),
        NavigationItem(
            route = "statistics_page",
            label = "Statistik",
            icon = Icons.Default.BarChart
        ),
        NavigationItem(
            route = "pomodoro_clock",
            label = "Pomodoro",
            icon = Icons.Default.Timer
        ),
//        NavigationItem(
//            route = "profile",
//            label = "Profile",
//            icon = Icons.Default.Person
//        )
    )

    NavigationBar(
        modifier = Modifier
            .height(130.dp)
            .clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)),

    ) {
        navigationItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(25.dp)
                    )
                },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

// Data class for Navigation Item
data class NavigationItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
