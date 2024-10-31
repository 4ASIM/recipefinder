package com.example.recipefinder.database.MealPlan

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_plan_table")
data class MealPlan(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val mealTime: String,
    val dishId: Int,
    var dishName: String? = null,
    var dishImage: String? = null
)
