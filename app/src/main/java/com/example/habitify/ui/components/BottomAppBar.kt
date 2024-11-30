//package com.example.habitify.ui.components
//
//import androidx.collection.isNotEmpty
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.heightIn
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.BarChart
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material.icons.filled.Timer
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import androidx.navigation.compose.currentBackStackEntryAsState
//import com.example.habitify.navigation.Screen
//
//@Composable
//fun BottomAppBar(navController: NavController, modifier: Modifier = Modifier) {
//    // Get the current back stack entry safely
//    val currentBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentRoute = currentBackStackEntry?.destination?.route
//
//    // Define navigation items
//    val navigationItems = listOf(
//        NavigationItem(
//            title = "Home",
//            icon = Icons.Default.Home,
//            screen = Screen("homepage")
//        ),
//        NavigationItem(
//            title = "Statistics",
//            icon = Icons.Default.BarChart,
//            screen = Screen("statistics_page")
//        ),
//        NavigationItem(
//            title = "Pomodoro",
//            icon = Icons.Default.Timer,
//            screen = Screen("pomodoro_clock")
//        ),
//        NavigationItem(
//            title = "Profile",
//            icon = Icons.Default.Person,
//            screen = Screen("profile")
//        )
//    )
//
//    NavigationBar(
//        modifier = modifier
//            .background(Color.White)
//            .heightIn(min = 56.dp)
//) {
//        navigationItems.forEach { item ->
//            NavigationBarItem(
//                icon = { Icon(imageVector = item.icon, contentDescription = item.title, modifier = Modifier.size(24.dp)) },
//                label = { Text(item.title, style = MaterialTheme.typography.labelSmall) },
//                selected = currentRoute == item.screen.route,
//                onClick = {
//                    // Check if NavHost is initialized before navigating
//                    if (navController.graph.nodes.isNotEmpty()) {
//                        navController.navigate(item.screen.route) {
//                            popUpTo("homepage") { inclusive = false }
//                            launchSingleTop = true
//                            restoreState = true
//                        }
//                    } else {
//                        // Optional: Handle navigation attempts before initialization
//                        println("Navigation graph not yet set!")
//                    }
//                },
//                modifier = Modifier.padding(8.dp)
//            )
//        }
//    }
//}
//
