package com.example.habitify.data.model.local

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.time.LocalDate

@Database(entities = [Habit::class], version = 2, exportSchema = false)
abstract class HabitDB : RoomDatabase() {
    abstract fun habitDAO(): HabitDAO

    companion object {
        @Volatile
        private var INSTANCE: HabitDB? = null

        fun getDatabase(context: Context): HabitDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HabitDB::class.java,
                    "habit_database"
                )
                    .addMigrations(MIGRATION_1_2) // Add the migration here
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun migrate(db: SupportSQLiteDatabase) {
                // Add the `date` column to the existing `habits` table
                db.execSQL("ALTER TABLE habits ADD COLUMN date TEXT DEFAULT '${LocalDate.now()}' NOT NULL")
            }
        }
    }
}
