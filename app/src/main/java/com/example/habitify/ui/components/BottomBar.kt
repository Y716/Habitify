package com.example.habitify.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.habitify.AppNavigation
import com.example.habitify.AuthViewModel
import com.example.habitify.viewmodel.HabitViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomBar(authViewModel: AuthViewModel, viewModel: HabitViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .height(130.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
            ) {
                BottomNav(navController = navController)
            }
        },
        floatingActionButton = {
            Box() {
                FloatingActionButton(
                    onClick = {
                        navController.navigate("homepage")
                    },
                    shape = CircleShape,
                    modifier = Modifier
                        .align(
                            Alignment.Center)
                        .size(90.dp)
                        .offset(y = 65.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Homepage",
                        modifier = Modifier.size(45.dp)
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        AppNavigation(
            modifier = Modifier.padding(paddingValues),
            authViewModel = authViewModel,
            viewModel = viewModel,
            navController = navController
        )
    }
}