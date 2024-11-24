package com.example.habitify.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitify.data.model.local.Habit
import com.example.habitify.data.model.local.HabitRepository
import com.example.habitify.data.model.local.HabitStatus
import kotlinx.coroutines.launch
import java.time.LocalDate

class HabitViewModel(private val repository: HabitRepository) : ViewModel() {


    // LiveData for all habits fetched from the repository
    val allHabits: LiveData<List<Habit>> = repository.getAllHabits()

    fun getHabitsForDate(date: LocalDate): LiveData<List<Pair<Habit, HabitStatus?>>> {
        return repository.getHabitsForDate(date)
    }

    // LiveData to hold the habits and their statuses for the selected date
    private val _habitsForDate = MutableLiveData<List<Pair<Habit, HabitStatus?>>>()
    val habitsForDate: LiveData<List<Pair<Habit, HabitStatus?>>>
        get() = _habitsForDate

    // Load habits for the selected date
    fun loadHabitsForDate(date: LocalDate) {
        viewModelScope.launch {
            val habitsWithStatus = repository.getHabitsForDateSync(date) // Fetch from repository
            _habitsForDate.postValue(habitsWithStatus) // Update LiveData
        }
    }

    fun addHabit(title: String) {
        viewModelScope.launch {
            repository.insertHabit(Habit(title = title))
        }
    }

    fun toggleHabitCompletion(habitId: Int, date: LocalDate, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.updateHabitStatus(habitId, date.toString(), isCompleted)
        }
    }

    fun addHabitWithStatus(title: String, date: String) {
        viewModelScope.launch {
            // Add the habit to the habits table
            val habitId = repository.addHabitAndGetId(Habit(title = title))

            // Add the status for the selected date
            repository.addHabitStatus(habitId, date, isCompleted = false)
        }
    }

}