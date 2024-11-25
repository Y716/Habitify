package com.example.habitify.pages

import android.os.Build
import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.platform.LocalContext
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

//        BottomBar(navController = navController)
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
                val isFuture = date > today // Check if the date is in the future

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(48.dp)
                        .padding(horizontal = 2.dp)
                ) {
                    Text(
                        text = date.dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault()),
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Date number (disable future dates)
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isFuture -> Color.LightGray // Future dates are light gray
                                    isToday -> Color.Cyan
                                    isSelected -> Color(0xFF90CAF9)
                                    else -> Color.LightGray
                                }
                            )
                            .clickable(enabled = !isFuture) { // Disable clicking on future dates
                                if (!isFuture) onDateSelected(date)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
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
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = Color(0xFF64B5F6),
                    trackColor = Color(0xFFE3F2FD),
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



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HabitListSection(viewModel: HabitViewModel, selectedDate: LocalDate) {
    val today = LocalDate.now()
    val isFutureDate = selectedDate > today // Check if the selected date is in the future
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
                        if (!isFutureDate) viewModel.toggleHabitCompletion(habit.id, selectedDate, isCompleted)
                    },
                    onDelete = {
                        if (!isFutureDate) viewModel.deleteHabit(habit.id)
                    },
                    isEditable = !isFutureDate
                )
                if (habitsWithStatus.last() != (habit to status)) {
                    Divider(color = Color(0xFFE0E0E0))
                }
            }
        }
    }
}




@Composable
fun HabitItem(habit: Habit, isCompleted: Boolean, onToggle: (Boolean) -> Unit, onDelete: () -> Unit, isEditable: Boolean) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = habit.title, style = MaterialTheme.typography.bodyMedium)

        Row(verticalAlignment = Alignment.CenterVertically) {
            // Toggle switch (disable for future dates)
            Switch(
                checked = isCompleted,
                onCheckedChange = { if (isEditable) onToggle(it) },
                enabled = isEditable,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF1976D2),
                    checkedTrackColor = Color(0xFF90CAF9),
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFFE0E0E0)
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Delete button (disable for future dates)
            IconButton(
                onClick = {
                    if (isEditable) {
                        onDelete()
                        Toast.makeText(context, "${habit.title} deleted", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = isEditable
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Habit", tint = if (isEditable) Color.Red else Color.Gray)
            }
        }
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
