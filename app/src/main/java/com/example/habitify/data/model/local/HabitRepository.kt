package com.example.habitify.data.model.local

import androidx.lifecycle.LiveData
import java.time.LocalDate

class HabitRepository(private val habitDAO: HabitDAO) {

    val allHabits: LiveData<List<Habit>> = habitDAO.getAllHabits()

    suspend fun insert(habit: Habit) {
        habitDAO.insertHabit(habit)
    }

    suspend fun delete(habit: Habit) {
        habitDAO.deleteHabit(habit)
    }

    suspend fun getHabitsForDate(date: LocalDate): List<Habit> {
        return habitDAO.getHabitsForDate(date.toString()) // Assuming the date is stored as a String
    }

    suspend fun updateHabitStatus(habitId: Int, isCompleted: Boolean) {
        habitDAO.updateHabitStatus(habitId, isCompleted)
    }

    fun syncHabits() {
        TODO("Not yet implemented")
    }
}
