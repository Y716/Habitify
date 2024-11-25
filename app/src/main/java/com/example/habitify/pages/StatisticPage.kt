package com.example.habitify.pages

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habitify.data.model.local.HabitWeeklyStat
import com.example.habitify.ui.theme.HeatmapHigh
import com.example.habitify.ui.theme.HeatmapLow
import com.example.habitify.ui.theme.HeatmapMedium
import com.example.habitify.ui.theme.HeatmapVeryHigh
import com.example.habitify.ui.theme.HeatmapVeryLow
import com.example.habitify.viewmodel.HabitViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticsPage(viewModel: HabitViewModel, navController: NavController) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = rememberLazyListState()
        ) {
            // Overview Statistics Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    OverviewStatistics(viewModel = viewModel)
                }
            }

            // Monthly Statistics Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Statistik Bulanan",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        MonthlyCalendar(
                            onDateSelected = { date -> selectedDate = date },
                            viewModel = viewModel,
                            selectedDate = selectedDate
                        )
                    }
                }
            }

            // Weekly Statistics Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Statistik Mingguan",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        WeeklyStatistics(
                            viewModel = viewModel,
                            selectedMonth = selectedDate.withDayOfMonth(1)
                        )
                    }
                }
            }

            // Top Habits Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Habit Terbaik",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        TopHabits(viewModel = viewModel)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


@Composable
fun OverviewStatistics(viewModel: HabitViewModel) {
    val totalStreak by viewModel.getTotalStreak().observeAsState(0)
    val totalHabitsDone by viewModel.getTotalHabitsDone().observeAsState(0)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            title = "Total Streak Days",
            value = "${totalStreak} days" + if (totalStreak > 3) " 🔥" else "",
            icon = Icons.Default.CalendarToday,
            iconTint = MaterialTheme.colorScheme.primary
        )

        StatCard(
            modifier = Modifier.weight(1f),
            title = "Total Habits Done",
            value = "$totalHabitsDone",
            icon = Icons.Default.Check,
            iconTint = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    iconTint: Color
) {
    Card(
        modifier = modifier
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEDE7F6)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally // Changed to center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center // Added text alignment
            )
        }
    }
}



@SuppressLint("NewApi")
@Composable
fun MonthlyCalendar(
    onDateSelected: (LocalDate) -> Unit,
    viewModel: HabitViewModel,
    selectedDate: LocalDate
) {
    val today = LocalDate.now()
    var startOfMonth by remember { mutableStateOf(today.withDayOfMonth(1)) } // Awal bulan
    val daysInMonth = (0 until startOfMonth.lengthOfMonth()).map { startOfMonth.plusDays(it.toLong()) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Navigasi Bulan
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { startOfMonth = startOfMonth.minusMonths(1) }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Previous Month",
                    tint = Color.Gray
                )
            }

            Text(
                text = "${startOfMonth.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${startOfMonth.year}",
                style = MaterialTheme.typography.bodyMedium
            )

            IconButton(onClick = { startOfMonth = startOfMonth.plusMonths(1) }) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Next Month",
                    tint = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Tampilkan Grid Kalender
        LazyVerticalGrid(
            columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp),
            contentPadding = PaddingValues(4.dp)
        ) {
            items(daysInMonth) { date ->
                val habitsWithStatus by viewModel.getHabitsForDate(date).observeAsState(emptyList())
                val totalHabits = habitsWithStatus.size
                val completedHabits = habitsWithStatus.count { it.second?.isCompleted == true }
                val completionRate = if (totalHabits > 0) completedHabits.toFloat() / totalHabits else 0f

                // Pilihan warna heatmap
                val heatmapColor = when {
                    completionRate > 0.8f -> HeatmapVeryHigh
                    completionRate > 0.6f -> HeatmapHigh
                    completionRate > 0.4f -> HeatmapMedium
                    completionRate > 0.2f -> HeatmapLow
                    else -> HeatmapVeryLow
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable { onDateSelected(date) }
                ) {
                    // Tampilkan tanggal dengan heatmap
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                heatmapColor,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Indikator Progress Completion Rate
                    LinearProgressIndicator(
                        progress = completionRate,
                        modifier = Modifier
                            .width(36.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = Color(0xFF64B5F6),
                        trackColor = Color(0xFFB3E5FC)
                    )
                }
            }
        }
    }
}


@Composable
fun HeatmapLegend() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(
            HeatmapVeryLow to "Sangat Rendah",
            HeatmapLow to "Rendah",
            HeatmapMedium to "Sedang",
            HeatmapHigh to "Tinggi",
            HeatmapVeryHigh to "Sangat Tinggi"
        ).forEach { (color, label) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeeklyStatistics(
    viewModel: HabitViewModel,
    selectedMonth: LocalDate
) {
    var selectedWeek by remember { mutableStateOf(0) } // Minggu ke berapa yang dipilih
    val startOfMonth = selectedMonth.withDayOfMonth(1)
    val weeksInMonth = (0 until startOfMonth.lengthOfMonth() / 7).toList()

    // Ambil tanggal awal dan akhir minggu berdasarkan pilihan
    val startDate = startOfMonth.plusDays((selectedWeek * 7).toLong())
    val endDate = startDate.plusDays(6)
    println("Selected Week: $selectedWeek, Start Date: $startDate, End Date: $endDate")
    val weeklyStats by viewModel.getWeeklyStatistics(startDate, endDate).observeAsState(emptyList())

    Column(modifier = Modifier.fillMaxWidth()) {
        // Dropdown untuk memilih minggu
        WeeklyDropdownMenuMaterial(
            selectedWeek = selectedWeek,
            onWeekSelected = { selectedWeek = it },
            weeksInMonth = weeksInMonth
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Bar Chart
        BarChart(data = weeklyStats)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyDropdownMenuMaterial(
    selectedWeek: Int,
    onWeekSelected: (Int) -> Unit,
    weeksInMonth: List<Int>
) {
    var expanded by remember { mutableStateOf(false) }
    val weekOptions = weeksInMonth.map { "Minggu ${it + 1}" }
    var selectedOption by remember { mutableStateOf(weekOptions[selectedWeek]) }

    // Exposed Dropdown Menu Box
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        // TextField untuk dropdown
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {}, // Tidak digunakan karena pilihan hanya dari dropdown
            readOnly = true, // Pastikan hanya bisa dipilih dari dropdown
            label = { Text("Pilih Minggu") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor() // Untuk menyesuaikan dropdown dengan TextField
                .fillMaxWidth() // Opsional, jika ingin penuh
        )

        // Dropdown Menu
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false } // Tutup menu saat klik di luar
        ) {
            weekOptions.forEachIndexed { index, option ->
                DropdownMenuItem(
                    onClick = {
                        selectedOption = option
                        onWeekSelected(index)
                        expanded = false // Tutup dropdown setelah memilih
                    },
                    text = { Text(text = option) }
                )
            }
        }
    }
}


@Composable
fun BarChart(data: List<HabitWeeklyStat>) {
    val maxValue = data.maxOfOrNull { it.completedCount } ?: 1

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Tinggi total barchart, tambahkan sedikit margin jika diperlukan
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom // Pastikan batang dimulai dari dasar
    ) {
        data.forEach { stat ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f) // Atur lebar batang proporsional
            ) {
                // Angka jumlah di atas batang
                Text(
                    text = stat.completedCount.toString(),
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Batang
                Box(
                    modifier = Modifier
                        .width(24.dp) // Lebar batang
                        .height((stat.completedCount.toFloat() / maxValue * 150).dp) // Batasi tinggi batang
                        .background(MaterialTheme.colorScheme.primary)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Label habit di bawah batang
                Text(
                    text = stat.habitTitle,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 4.dp) // Tambahkan padding untuk memastikan ruang
                )
            }
        }
    }
}



@Composable
fun TopHabits(viewModel: HabitViewModel) {
    val topHabits by viewModel.getTopHabits().observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        topHabits.forEachIndexed { index, habit ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${index + 1}. ${habit.habitTitle}",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                            color = Color(0xFF424242)
                        )
                        Text(
                            text = "Completion Count: ${habit.completionCount}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF757575)
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ShowChart,
                        contentDescription = "Habit Chart Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

