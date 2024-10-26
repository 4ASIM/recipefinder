package com.example.recipefinder.database.DishDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dishes")
data class DishEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val image: String,
    val cuisine: String
)

