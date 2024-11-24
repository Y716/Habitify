package com.example.habitify.data.model.local

import androidx.room.ColumnInfo

// Model untuk statistik bulanan
data class DateStatistic(
    val date: String,   // Format tanggal: "yyyy-MM-dd"
    val total: Int,     // Total habit pada tanggal ini
    val completed: Int  // Habit yang berhasil diselesaikan pada tanggal ini
)

// Model untuk Top Habits
data class TopHabit(
    @ColumnInfo(name = "habitTitle") // Sesuaikan nama kolom dengan alias di query
    val habitTitle: String,

    @ColumnInfo(name = "completionCount") // Sesuaikan nama kolom dengan alias di query
    val completionCount: Int
)



data class HabitStatusQueryResult(
    val habitId: Int,
    val isCompleted: Boolean,
    val date: String
)

data class HabitWeeklyStat(
    val habitTitle: String,
    val completedCount: Int
)
