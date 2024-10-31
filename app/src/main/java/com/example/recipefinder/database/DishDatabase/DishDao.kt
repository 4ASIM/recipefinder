package com.example.recipefinder.database.DishDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipefinder.retrofit.Recipe
import kotlinx.coroutines.flow.Flow
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

    @Query("SELECT * FROM dishes")
    fun getAllDishesFlow(): Flow<List<Recipe>>

    @Query("SELECT * FROM dishes WHERE id = :dishId")
    suspend fun getDishById(dishId: Long): Recipe?

    @Query("SELECT * FROM dishes WHERE id = :dishId LIMIT 1")
    suspend fun getDishById(dishId: Int): Recipe?

}
