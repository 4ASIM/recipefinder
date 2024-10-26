package com.example.recipefinder.database.DishDatabase

import android.app.Application

class DishApplication : Application() {
    val database by lazy { DishDatabase.getDatabase(this) }
    val repository by lazy { DishRepository(database.dishDao(),database.ingredientDao(),database.instructionDao()) }
}
