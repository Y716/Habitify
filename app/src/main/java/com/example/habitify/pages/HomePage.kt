package com.example.habitify.pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habitify.AuthViewModel
import com.example.habitify.data.model.local.Habit
import com.example.habitify.viewmodel.HabitViewModel
import java.time.LocalDate
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomePage(modifier: Modifier, navController: NavController, authViewModel: AuthViewModel, viewModel: HabitViewModel){
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    Column( modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween) {
        ProfileSection()
        DateSection { date ->
            selectedDate = date
            viewModel.loadHabitsForDate(date) // Load habits for the selected date
        }
        // Updated Daily Progress Section
        DailyProgressSection(viewModel = viewModel)

        // Updated Habit List Section
        HabitListSection(viewModel = viewModel)

        AddHabitButton(onClick = {
            navController.navigate("add_habit/${selectedDate.toString()}") // Pass selectedDate as a route parameter
        })

        BottomBar(navController = navController)
    }
}

@Composable
fun ProfileSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile Picture
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "P", style = MaterialTheme.typography.headlineMedium, color = Color.White)
        }

        // Placeholder for Other Profile Elements
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = "Hello, User!", style = MaterialTheme.typography.headlineLarge)
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateSection(
    onDateSelected: (LocalDate) -> Unit
) {
    val today = LocalDate.now()
    var startOfWeek by remember { mutableStateOf(today.minusDays(today.dayOfWeek.value.toLong() - 1)) }
    val daysOfWeek = (0..6).map { startOfWeek.plusDays(it.toLong()) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Navigation Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { startOfWeek = startOfWeek.minusWeeks(1) }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Previous Week",
                    tint = Color.Gray
                )
            }

            Text(
                text = "${startOfWeek.dayOfMonth} ${startOfWeek.month.name.take(3)} - ${startOfWeek.plusDays(6).dayOfMonth} ${startOfWeek.plusDays(6).month.name.take(3)}",
                style = MaterialTheme.typography.bodyMedium
            )

            IconButton(onClick = { startOfWeek = startOfWeek.plusWeeks(1) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next Week",
                    tint = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Weekdays Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp), // Reduced horizontal padding
            horizontalArrangement = Arrangement.SpaceEvenly // Changed to SpaceEvenly for equal spacing
        ) {
            daysOfWeek.forEach { date ->
                val isToday = date == today
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(48.dp) // Reduced width to accommodate all days
                        .padding(horizontal = 2.dp) // Added small horizontal padding
                ) {
                    Text(
                        text = date.dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault()),
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .size(36.dp) // Slightly reduced size
                            .clip(CircleShape)
                            .background(if (isToday) Color.Cyan else Color.LightGray)
                            .clickable { onDateSelected(date) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            style = MaterialTheme.typography.bodySmall // Smaller text size
                        )
                    }
                }
            }
        }
    }
}





@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyProgressSection(viewModel: HabitViewModel) {
    val habits by viewModel.allHabits.observeAsState(emptyList()) // Observe all habits
    val progress = if (habits.isNotEmpty()) {
        habits.count { it.isCompleted }.toFloat() / habits.size // Calculate progress
    } else {
        0f
    }
    val today = java.time.LocalDate.now() // Current date

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFFE0E0E0), shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)) // Card-like background
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Big square for the date
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                    .background(Color(0xFFBDBDBD)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = today.dayOfMonth.toString(),
                    style = androidx.compose.material3.MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Progress and motivational text
            Column(modifier = Modifier.weight(1f)) {
                // Motivational saying
                Text(
                    text = "Keep going, you're doing great!",
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                    color = Color(0xFF616161),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Highlighted progress bar
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(6.dp)),
                    color = Color(0xFF64B5F6), // Progress bar color
                    trackColor = Color(0xFFB3E5FC) // Track color
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Display percentage
                Text(
                    text = "Progress: ${(progress * 100).toInt()}%",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF424242)
                )
            }
        }
    }
}





@Composable
fun HabitListSection(viewModel: HabitViewModel) {
    val habits by viewModel.allHabits.observeAsState(initial = emptyList())

    LazyColumn {
        items(habits) { habit ->
            HabitItem(
                habit = habit,
                onToggle = { isCompleted ->
                    viewModel.updateHabitStatus(habit.id, isCompleted)
                }
            )
        }
    }
}

@Composable
fun HabitItem(habit: Habit, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = habit.title, style = MaterialTheme.typography.bodyMedium)
        Switch(
            checked = habit.isCompleted,
            onCheckedChange = { onToggle(it) }
        )
    }
}


@Composable
fun AddHabitButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Tambah Habit")
    }
}


@Composable
fun BottomBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("homepage") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("statistics_page") },
            icon = { Icon(Icons.Default.BarChart, contentDescription = "Statistics") },
            label = { Text("Statistics") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("pomodoro_clock") },
            icon = { Icon(Icons.Default.Timer, contentDescription = "Pomodoro") },
            label = { Text("Pomodoro") }
        )
    }
}


