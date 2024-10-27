package com.example.recipefinder.database.DishDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipefinder.retrofit.Recipe

@Dao
interface DishDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertDishes(dishes: List<DishEntity>)

    @Query("SELECT * FROM dishes WHERE cuisine = :cuisine")
    fun getDishesByCuisine(cuisine: String): List<DishEntity>

    @Query("SELECT * FROM dishes")
    fun getAllDishes(): List<Recipe>

    @Query("SELECT id FROM dishes")
    suspend fun getAllDishIds(): List<Int>

    @Query("SELECT COUNT(*) FROM dishes")
    suspend fun getDishCount(): Int
}
