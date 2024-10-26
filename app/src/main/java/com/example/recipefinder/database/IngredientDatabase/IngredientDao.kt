package com.example.recipefinder.database.IngredientDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface IngredientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(ingredients: List<IngredientEntity>)

    @Query("SELECT * FROM ingredients WHERE dishId = :dishId")
    suspend fun getIngredientsForDish(dishId: Int): List<IngredientEntity>
}

