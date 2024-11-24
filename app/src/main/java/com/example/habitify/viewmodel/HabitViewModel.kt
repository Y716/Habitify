package com.example.habitify.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitify.data.model.local.Habit
import com.example.habitify.data.model.local.HabitRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

class HabitViewModel(private val repository: HabitRepository) : ViewModel() {

    private val _habitsForSelectedDate = MutableLiveData<List<Habit>>()
    val habitsForSelectedDate: LiveData<List<Habit>> = _habitsForSelectedDate

    // LiveData for all habits fetched from the repository
    val allHabits: LiveData<List<Habit>> = repository.allHabits

    // Daily progress (can be calculated based on habits or set manually)
    private val _dailyProgress = MutableLiveData(0.0f) // Default progress
    val dailyProgress: LiveData<Float> get() = _dailyProgress

    /**
     * Add a new habit to the repository.
     */
    fun addHabit(habit: Habit) {
        viewModelScope.launch {
            repository.insert(habit)
            calculateDailyProgress() // Recalculate progress after adding a habit
        }
    }

    /**
     * Delete a habit from the repository.
     */
    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            repository.delete(habit)
            calculateDailyProgress() // Recalculate progress after deleting a habit
        }
    }

    /**
     * Update the completion status of a habit.
     */
    fun updateHabitStatus(habitId: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.updateHabitStatus(habitId, isCompleted)
            calculateDailyProgress() // Recalculate progress after updating a habit
        }
    }

    /**
     * Sync habits from a remote source (optional).
     */
    fun syncHabits() {
        viewModelScope.launch {
            repository.syncHabits()
            calculateDailyProgress() // Ensure progress is updated after syncing
        }
    }

    // Load habits for a specific date
    fun loadHabitsForDate(date: LocalDate) {
        viewModelScope.launch {
            val habits = repository.getHabitsForDate(date) // Replace with your logic to filter by date
            _habitsForSelectedDate.postValue(habits)
        }
    }

    // Update habit status for a specific date
    fun updateHabitForDate(date: LocalDate, habitId: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.updateHabitStatus(habitId, isCompleted) // Adjust logic if specific date is needed
            loadHabitsForDate(date) // Refresh habits for the selected date
        }
    }

    /**
     * Calculate daily progress based on completed habits.
     */
    private fun calculateDailyProgress() {
        viewModelScope.launch {
            val habits = repository.allHabits.value ?: emptyList()
            val completedCount = habits.count { it.isCompleted }
            val totalCount = habits.size
            if (totalCount > 0) {
                _dailyProgress.value = completedCount.toFloat() / totalCount
            } else {
                _dailyProgress.value = 0f // Reset progress if there are no habits
            }
        }
    }
}