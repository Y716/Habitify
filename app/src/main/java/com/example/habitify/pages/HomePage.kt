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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import com.example.habitify.AuthViewModel
import com.example.habitify.data.model.local.Habit
import com.example.habitify.viewmodel.HabitViewModel
import java.time.LocalDate
import java.util.Locale
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.SwitchDefaults
import androidx.compose.foundation.border
import androidx.compose.ui.text.font.FontWeight

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomePage(modifier: Modifier, navController: NavController, authViewModel: AuthViewModel, viewModel: HabitViewModel){
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA)) // Light background color for the whole page
            .padding(top = 16.dp), // Add top padding but remove horizontal padding
        verticalArrangement = Arrangement.Top // Change to Top arrangement
    ) {
        ProfileSection()
        DateSection(
            onDateSelected = { date ->
                selectedDate = date
                viewModel.loadHabitsForDate(date)
            },
            viewModel = viewModel,
            selectedDate = selectedDate
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Wrap the content in a scrollable column
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { DailyProgressSection(viewModel = viewModel, selectedDate = selectedDate) }
            item { HabitListSection(viewModel = viewModel, selectedDate = selectedDate) }
            item {
                Spacer(modifier = Modifier.height(8.dp))
                AddHabitButton(onClick = {
                    navController.navigate("add_habit/${selectedDate.toString()}")
                })
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        BottomBar(navController = navController)
    }
}

@Composable
fun ProfileSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 12.dp), // Adjusted padding
        horizontalArrangement = Arrangement.Start, // Changed to Start alignment
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile Picture
        Box(
            modifier = Modifier
                .size(40.dp) // Reduced size to match design
                .clip(CircleShape)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "P",
                style = MaterialTheme.typography.bodyLarge, // Smaller text size
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.width(12.dp)) // Reduced spacing

        // Greeting Text
        Text(
            text = "Hello, User!",
            style = MaterialTheme.typography.titleLarge, // Changed text style
            color = Color.Black
        )
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateSection(
    onDateSelected: (LocalDate) -> Unit,
    viewModel: HabitViewModel, // Pass the ViewModel to fetch data
    selectedDate: LocalDate
) {
    val today = LocalDate.now()
    var startOfWeek by remember { mutableStateOf(today.minusDays(today.dayOfWeek.value.toLong() - 1)) } // Start of the current week
    val daysOfWeek = (0..6).map { startOfWeek.plusDays(it.toLong()) } // Generate the week dates

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
                    imageVector = Icons.Filled.ArrowForward,
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
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            daysOfWeek.forEach { date ->
                val isToday = date == today
                val isSelected = date == selectedDate

                // Fetch habits and their statuses for this date
                val habitsWithStatus by viewModel.getHabitsForDate(date).observeAsState(emptyList())
                val totalHabits = habitsWithStatus.size
                val completedHabits = habitsWithStatus.count { it.second?.isCompleted == true }
                val completionRate = if (totalHabits > 0) completedHabits.toFloat() / totalHabits else 0f

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(48.dp)
                        .padding(horizontal = 2.dp)
                ) {
                    // Day of the week (e.g., Mon, Tue)
                    Text(
                        text = date.dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault()),
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Date number (Clickable to select a specific date)
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isToday -> Color.Cyan // Highlight today's date
                                    isSelected -> Color(0xFF90CAF9) // Highlight the selected date
                                    else -> Color.LightGray // Default color
                                }
                            )
                            .clickable {
                                onDateSelected(date) // Pass the selected date to the parent
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Completion indicator (progress bar below the date)
                    LinearProgressIndicator(
                        progress = completionRate,
                        modifier = Modifier
                            .width(36.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = Color(0xFF64B5F6), // Progress color
                        trackColor = Color(0xFFB3E5FC) // Background track color
                    )
                }
            }
        }
    }
}


@Composable
fun DailyProgressSection(viewModel: HabitViewModel, selectedDate: LocalDate) {
    val habitsWithStatus by viewModel.getHabitsForDate(selectedDate).observeAsState(emptyList())
    val totalHabits = habitsWithStatus.size
    val completedHabits = habitsWithStatus.count { it.second?.isCompleted == true }
    val progress = if (totalHabits > 0) completedHabits.toFloat() / totalHabits else 0f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF90CAF9)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = selectedDate.dayOfMonth.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Keep going, you're doing great!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF424242)
                )

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = Color(0xFF64B5F6),
                    trackColor = Color(0xFFE3F2FD)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Progress: ${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF757575)
                )
            }
        }
    }
}



@Composable
fun HabitListSection(viewModel: HabitViewModel, selectedDate: LocalDate) {
    val habitsWithStatus by viewModel.getHabitsForDate(selectedDate).observeAsState(emptyList())

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            habitsWithStatus.forEach { (habit, status) ->
                HabitItem(
                    habit = habit,
                    isCompleted = status?.isCompleted ?: false,
                    onToggle = { isCompleted ->
                        viewModel.toggleHabitCompletion(habit.id, selectedDate, isCompleted)
                    }
                )
                if (habitsWithStatus.last() != (habit to status)) {
                    Divider(color = Color(0xFFE0E0E0))
                }
            }
        }
    }
}

@Composable
fun HabitItem(habit: Habit, isCompleted: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = habit.title,
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF212121)
        )
        Switch(
            checked = isCompleted,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF1976D2),
                checkedTrackColor = Color(0xFF90CAF9),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFE0E0E0)
            )
        )
    }
}




@Composable
fun AddHabitButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1976D2)
        )
    ) {
        Text(
            text = "Tambah Habit",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )
    }
}


@Composable
fun BottomBar(navController: NavController) {
    NavigationBar(
        containerColor = Color.White,
        modifier = Modifier.border(
            width = 1.dp,
            color = Color(0xFFE0E0E0),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        )
    ) {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("homepage") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1976D2),
                unselectedIconColor = Color(0xFF757575),
                selectedTextColor = Color(0xFF1976D2),
                unselectedTextColor = Color(0xFF757575),
                indicatorColor = Color(0xFFE3F2FD)
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("statistics_page") },
            icon = { Icon(Icons.Default.BarChart, contentDescription = "Statistics") },
            label = { Text("Statistics") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1976D2),
                unselectedIconColor = Color(0xFF757575),
                selectedTextColor = Color(0xFF1976D2),
                unselectedTextColor = Color(0xFF757575),
                indicatorColor = Color(0xFFE3F2FD)
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("pomodoro_clock") },
            icon = { Icon(Icons.Default.Timer, contentDescription = "Pomodoro") },
            label = { Text("Pomodoro") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1976D2),
                unselectedIconColor = Color(0xFF757575),
                selectedTextColor = Color(0xFF1976D2),
                unselectedTextColor = Color(0xFF757575),
                indicatorColor = Color(0xFFE3F2FD)
            )
        )
    }
}


