package com.example.habitify.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(
    tableName = "habit_status",
    foreignKeys = [
        ForeignKey(
            entity = Habit::class,
            parentColumns = ["id"],
            childColumns = ["habitId"],
            onDelete = CASCADE
        )
    ]
)
data class HabitStatus(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val habitId: Int, // Link to Habit
    @ColumnInfo(name = "isCompleted")
    val isCompleted: Boolean,
    @ColumnInfo(name = "date")
    val date: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()), // Use SimpleDateFormat
    @ColumnInfo(name = "createdAt")
    val createdAt: Long = System.currentTimeMillis()
)



