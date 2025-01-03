package com.example.habitify.data.model.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface HabitDAO {
    @Query("SELECT * FROM habits")
    fun getAllHabits(): LiveData<List<Habit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit)

    @Query("DELETE FROM habits WHERE id = :habitId")
    suspend fun deleteHabitById(habitId: Int)

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

    @Query("SELECT * FROM habit_status WHERE habitId = :habitId AND date = :date LIMIT 1")
    suspend fun getHabitStatusForDateAndHabitId(habitId: Int, date: String): HabitStatus?

    @Update
    suspend fun updateHabitStatus(habitStatus: HabitStatus)

    @Query("""
        SELECT date, 
               COUNT(habitId) AS total, 
               SUM(CASE WHEN isCompleted THEN 1 ELSE 0 END) AS completed
        FROM habit_status
        WHERE date BETWEEN :startDate AND :endDate
        GROUP BY date
        ORDER BY date ASC
    """)
    fun getMonthlyStatistics(startDate: String, endDate: String): LiveData<List<DateStatistic>>

    @Query("""
        SELECT h.title AS habitTitle, 
               COALESCE(COUNT(CASE WHEN hs.isCompleted = 1 THEN 1 END), 0) AS completedCount
        FROM habits h
        LEFT JOIN habit_status hs ON hs.habitId = h.id AND hs.date BETWEEN :startDate AND :endDate
        GROUP BY h.id
    """)
    fun getWeeklyStatistics(startDate: String, endDate: String): LiveData<List<HabitWeeklyStat>>



    @Query("""
        SELECT h.title AS habitTitle, COUNT(*) AS completionCount
        FROM habit_status hs
        INNER JOIN habits h ON hs.habitId = h.id
        WHERE hs.isCompleted = 1
        GROUP BY h.title
        ORDER BY completionCount DESC
        LIMIT 3
    """)
    fun getTopHabits(): LiveData<List<TopHabit>>




    @Query("SELECT * FROM habit_status")
    fun getAllHabitStatuses(): LiveData<List<HabitStatus>>

    @Query("""
        WITH total_habits AS (
            SELECT COUNT(*) AS total_count FROM habits
        ),
        completed_dates AS (
            SELECT date,
                CASE WHEN (
                    SELECT COUNT(DISTINCT habitId)
                    FROM habit_status
                    WHERE date = hs.date AND isCompleted = 1
                ) = (SELECT total_count FROM total_habits)
                THEN 1 ELSE 0 END AS is_full
            FROM (SELECT DISTINCT date FROM habit_status) hs
        ),
        streak_dates AS (
            SELECT date, is_full
            FROM completed_dates
            WHERE date = DATE('now', 'localtime') AND is_full = 1
            UNION ALL
            SELECT cd.date, cd.is_full
            FROM completed_dates cd
            JOIN streak_dates sd ON cd.date = DATE(sd.date, '-1 day')
            WHERE cd.is_full = 1
        )
        SELECT COUNT(*) AS total_streak_days
        FROM streak_dates
        WHERE is_full = 1;
    """)
    fun getTotalStreakDays(): LiveData<Int>









    // Best streak (jumlah hari berturut-turut terbaik)
//    @Query("""
//        SELECT MAX(streak)
//        FROM (
//            SELECT date, ROW_NUMBER() OVER (ORDER BY date) -
//            ROW_NUMBER() OVER (PARTITION BY isCompleted ORDER BY date) AS streak_group
//            FROM habit_status
//            WHERE isCompleted = 1
//        ) AS grouped
//        GROUP BY streak_group
//    """)
//    fun getBestStreak(): LiveData<Int>

    // Total jumlah kebiasaan yang selesai
    @Query("""
        SELECT COUNT(*) 
        FROM habit_status
        WHERE isCompleted = 1
    """)
    fun getTotalHabitsDone(): LiveData<Int>

}
