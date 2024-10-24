package com.example.recipefinder.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DishDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertDishes(dishes: List<DishEntity>)

    @Query("SELECT * FROM dishes WHERE cuisine = :cuisine")
    fun getDishesByCuisine(cuisine: String): List<DishEntity>
}
