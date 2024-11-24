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

@Database(entities = [Habit::class, HabitStatus::class], version = 3, exportSchema = false)
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
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3) // Add the migration here
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Add the `habit_status` table
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS habit_status (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        habitId INTEGER NOT NULL,
                        date TEXT NOT NULL,
                        isCompleted INTEGER NOT NULL,
                        FOREIGN KEY(habitId) REFERENCES habits(id) ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
            }
        }
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Step 1: Create a new table with the updated schema
                db.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS habits_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                title TEXT NOT NULL,
                createdAt INTEGER NOT NULL DEFAULT 0
            )
            """.trimIndent()
                )

                // Step 2: Copy existing data into the new table
                db.execSQL(
                    """
            INSERT INTO habits_new (id, title, createdAt)
            SELECT id, title, ${System.currentTimeMillis()} FROM habits
            """.trimIndent()
                )

                // Step 3: Drop the old table
                db.execSQL("DROP TABLE habits")

                // Step 4: Rename the new table to match the original name
                db.execSQL("ALTER TABLE habits_new RENAME TO habits")

                // Step 5: Create the `habit_status` table
                db.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS habit_status (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                habitId INTEGER NOT NULL,
                isCompleted INTEGER NOT NULL DEFAULT 0,
                date TEXT NOT NULL DEFAULT '',
                createdAt INTEGER NOT NULL DEFAULT 0,
                FOREIGN KEY(habitId) REFERENCES habits(id) ON DELETE CASCADE
            )
            """.trimIndent()
                )
            }
        }




    }
}
