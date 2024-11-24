package com.example.habitify.data.model.local

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

@Entity(tableName = "habits")
data class Habit @RequiresApi(Build.VERSION_CODES.O) constructor(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "isCompleted")
    val isCompleted: Boolean,
    @ColumnInfo(name = "date")
    val date: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()), // Use SimpleDateFormat
    @ColumnInfo(name = "createdAt")
    val createdAt: Long = System.currentTimeMillis()
)