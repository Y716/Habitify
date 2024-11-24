package com.example.habitify.data.model.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import java.time.LocalDate

class HabitRepository(private val habitDAO: HabitDAO) {

    fun getAllHabits(): LiveData<List<Habit>> = habitDAO.getAllHabits()

    fun getHabitsForDate(date: LocalDate): LiveData<List<Pair<Habit, HabitStatus?>>> {
        val habits = habitDAO.getAllHabits()
        val habitStatus = habitDAO.getHabitStatusForDate(date.toString())
        return combineLiveData(habits, habitStatus) { allHabits, statuses ->
            allHabits.map { habit ->
                val status = statuses.find { it.habitId == habit.id }
                habit to status
            }
        }
    }

    // Fetch habits and their statuses for a specific date (synchronously)
    suspend fun getHabitsForDateSync(date: LocalDate): List<Pair<Habit, HabitStatus?>> {
        val allHabits = habitDAO.getAllHabitsSync() // Get all habits
        val statusesForDate = habitDAO.getHabitStatusForDateSync(date.toString()) // Get statuses for the date
        return allHabits.map { habit ->
            val status = statusesForDate.find { it.habitId == habit.id }
            habit to status
        }
    }

    suspend fun insertHabit(habit: Habit) {
        habitDAO.insertHabit(habit)
    }

    suspend fun toggleHabitCompletion(habitId: Int, date: String, isCompleted: Boolean) {
        val existingStatus = habitDAO.getHabitStatusForDateAndHabitId(habitId, date)
        if (existingStatus != null) {
            habitDAO.updateHabitStatus(existingStatus.copy(isCompleted = isCompleted))
        } else {
            habitDAO.insertHabitStatus(
                HabitStatus(
                    habitId = habitId,
                    isCompleted = isCompleted,
                    date = date
                )
            )
        }
    }

    suspend fun deleteHabit(habitId: Int) {
        habitDAO.deleteHabitById(habitId)
    }


    // Add a habit and return its ID
    suspend fun addHabitAndGetId(habit: Habit): Int {
        return habitDAO.insertHabitAndGetId(habit).toInt()
    }

    // Add a habit status for a specific date
    suspend fun addHabitStatus(habitId: Int, date: String, isCompleted: Boolean) {
        habitDAO.insertHabitStatus(
            HabitStatus(habitId = habitId, date = date, isCompleted = isCompleted)
        )
    }

    fun <A, B, C> combineLiveData(
        liveData1: LiveData<A>,
        liveData2: LiveData<B>,
        combine: (A, B) -> C
    ): LiveData<C> {
        val mediator = MediatorLiveData<C>()
        var data1: A? = null
        var data2: B? = null

        mediator.addSource(liveData1) { value ->
            data1 = value
            if (data1 != null && data2 != null) {
                mediator.value = combine(data1!!, data2!!)
            }
        }

        mediator.addSource(liveData2) { value ->
            data2 = value
            if (data1 != null && data2 != null) {
                mediator.value = combine(data1!!, data2!!)
            }
        }

        return mediator
    }
}
