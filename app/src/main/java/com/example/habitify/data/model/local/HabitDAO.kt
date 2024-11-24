package com.example.habitify.data.model.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HabitDAO {
    @Query("SELECT * FROM habits")
    fun getAllHabits(): LiveData<List<Habit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    // HabitStatus
    @Query("SELECT * FROM habit_status WHERE date = :date")
    fun getHabitStatusForDate(date: String): LiveData<List<HabitStatus>>

    @Query("UPDATE habit_status SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateHabitStatus(id: Int, isCompleted: Boolean)

    // Get all habits synchronously
    @Query("SELECT * FROM habits")
    suspend fun getAllHabitsSync(): List<Habit>

    // Get all statuses for a specific date synchronously
    @Query("SELECT * FROM habit_status WHERE date = :date")
    suspend fun getHabitStatusForDateSync(date: String): List<HabitStatus>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabitAndGetId(habit: Habit): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabitStatus(habitStatus: HabitStatus)
}
