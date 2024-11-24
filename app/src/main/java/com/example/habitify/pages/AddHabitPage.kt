package com.example.habitify.pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habitify.viewmodel.HabitViewModel
import com.example.habitify.data.model.local.Habit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddHabitPage(
    navController: NavController,
    viewModel: HabitViewModel,
    selectedDate: String // Use selectedDate as a parameter
) {
    var habitTitle by remember { mutableStateOf("") } // State for the habit title

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA)) // Match HomePage background
            .padding(16.dp),
        contentAlignment = Alignment.Center // Center all components
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(), // Adjust height to wrap content
            verticalArrangement = Arrangement.spacedBy(24.dp), // Spacing between components
            horizontalAlignment = Alignment.CenterHorizontally // Center-align content
        ) {
            // Top Section with title
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Add a New Habit",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
                Text(
                    text = "Selected Date: $selectedDate",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            // Habit Input Section
            Card(
                modifier = Modifier.fillMaxWidth(0.9f), // Adjust card width
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Input field for the habit title
                    OutlinedTextField(
                        value = habitTitle,
                        onValueChange = { habitTitle = it },
                        label = { Text("Habit Title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Informative text
                    Text(
                        text = "Add a habit that you want to track regularly.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF616161)
                    )
                }
            }

            // Save Button directly under the card
            Button(
                onClick = {
                    if (habitTitle.isNotBlank()) {
                        // Add habit to the shared habits table
                        viewModel.addHabitWithStatus(habitTitle, selectedDate)

                        // Navigate back to the homepage
                        navController.navigate("homepage")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Text(
                    text = "Save Habit",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
        }
    }
}
