package com.example.habitify.data.model.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HabitDAO {
    @Query("SELECT * FROM habits ORDER BY createdAt DESC")
    fun getAllHabits(): LiveData<List<Habit>>

    @Query("SELECT * FROM habits WHERE date = :date ORDER BY createdAt DESC")
    suspend fun getHabitsForDate(date: String): List<Habit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("UPDATE habits SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateHabitStatus(id: Int, isCompleted: Boolean)
}
