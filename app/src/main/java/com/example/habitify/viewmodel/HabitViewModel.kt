package com.example.habitify.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitify.data.model.local.DateStatistic
import com.example.habitify.data.model.local.Habit
import com.example.habitify.data.model.local.HabitRepository
import com.example.habitify.data.model.local.HabitStatus
import com.example.habitify.data.model.local.HabitStatusQueryResult
import com.example.habitify.data.model.local.HabitWeeklyStat
import com.example.habitify.data.model.local.TopHabit
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

    @SuppressLint("NewApi")
    fun addHabit(title: String) {
        viewModelScope.launch {
            repository.insertHabit(Habit(title = title))
        }
    }

    fun toggleHabitCompletion(habitId: Int, date: LocalDate, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.toggleHabitCompletion(habitId, date.toString(), isCompleted)
        }
    }


    @SuppressLint("NewApi")
    fun addHabitWithStatus(title: String, date: String) {
        viewModelScope.launch {
            // Add the habit to the habits table
            val habitId = repository.addHabitAndGetId(Habit(title = title))

            // Add the status for the selected date
            repository.addHabitStatus(habitId, date, isCompleted = false)
        }
    }

    fun getMonthlyStatistics(startDate: String, endDate: String): LiveData<List<DateStatistic>> {
        return repository.getMonthlyStatistics(startDate, endDate)
    }

    fun getWeeklyStatistics(startDate: LocalDate, endDate: LocalDate): LiveData<List<HabitWeeklyStat>> {
        return repository.getWeeklyStatistics(startDate.toString(), endDate.toString())
    }


    fun getTopHabits(): LiveData<List<TopHabit>> {
        return repository.getTopHabits()
    }

    // LiveData untuk total streak
    fun getTotalStreak(): LiveData<Int> {
        return repository.getTotalStreak()
    }

//    // LiveData untuk best streak
//    fun getBestStreak(): LiveData<Int> {
//        return repository.getBestStreak()
//    }

    // LiveData untuk total habits done
    fun getTotalHabitsDone(): LiveData<Int> {
        return repository.getTotalHabitsDone()
    }

    fun deleteHabit(habitId: Int) {
        viewModelScope.launch {
            repository.deleteHabit(habitId)
        }
    }
}