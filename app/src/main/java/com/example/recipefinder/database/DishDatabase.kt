package com.example.recipefinder.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [DishEntity::class], version = 1)
abstract class DishDatabase : RoomDatabase() {
    abstract fun dishDao(): DishDao

    companion object {
        @Volatile
        private var INSTANCE: DishDatabase? = null

        fun getDatabase(context: Context): DishDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DishDatabase::class.java,
                    "dish_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
