package com.example.habitify.pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habitify.viewmodel.HabitViewModel
import com.example.habitify.data.model.local.Habit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddHabitPage(
    navController: NavController,
    viewModel: HabitViewModel,
    selectedDate: String // Add selectedDate parameter
) {
    var habitTitle by remember { mutableStateOf("") } // State for the habit title

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Add a New Habit", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Input field for the habit title
        OutlinedTextField(
            value = habitTitle,
            onValueChange = { habitTitle = it },
            label = { Text("Habit Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Save Habit Button
        Button(
            onClick = {
                if (habitTitle.isNotBlank()) {
                    val newHabit = Habit(
                        title = habitTitle,
                        isCompleted = false,
                        date = selectedDate.toString() // Use the selected date
                    )
                    viewModel.addHabit(newHabit)
                    navController.navigate("homepage")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Save Habit")
        }
    }
}
