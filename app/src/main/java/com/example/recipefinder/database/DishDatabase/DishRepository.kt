package com.example.recipefinder.database.DishDatabase

import com.example.recipefinder.database.IngredientDatabase.IngredientDao
import com.example.recipefinder.database.IngredientDatabase.IngredientEntity
import com.example.recipefinder.database.InstructionDatabase.InstructionDao
import com.example.recipefinder.database.InstructionDatabase.InstructionEntity
import com.example.recipefinder.network.RetrofitInstance


import android.util.Log
import com.example.recipefinder.database.favorite.SavedDishDao
import com.example.recipefinder.database.favorite.SavedDishEntity
import com.example.recipefinder.retrofit.Recipe

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DishRepository(
    private val dishDao: DishDao,
    private val ingredientDao: IngredientDao,
    private val cookingStepDao: InstructionDao,
    private val savedDishDao: SavedDishDao
) {
    private val apiKey = "9d9e99f4d79348318a0227e8886ba4ef"

    // Fetch dishes from API and save them to the database
    suspend fun fetchDishes(cuisines: List<String>) {
        val dishes = mutableListOf<DishEntity>()

        for (cuisine in cuisines) {
            val response = RetrofitInstance.spoonacularService.searchRecipes(cuisine, 5, apiKey)
            response.results.forEach { recipe ->
                dishes.add(DishEntity(recipe.id, recipe.title, recipe.image, recipe.maxReadyTime, cuisine))
            }
        }

        withContext(Dispatchers.IO) {
            dishDao.insertDishes(dishes)
        }
    }

    // Fetch ingredients and steps for dishes, but only if they are not already in the database
    suspend fun fetchAndSaveIngredientsAndSteps() {
        val dishIds = withContext(Dispatchers.IO) {
            dishDao.getAllDishIds()
        }

        for (dishId in dishIds) {
            // Check if ingredients for this dish already exist
            val existingIngredients = withContext(Dispatchers.IO) {
                ingredientDao.getIngredientsForDish(dishId)
            }

            if (existingIngredients.isNotEmpty()) {
                Log.d("DishRepository", "Ingredients for dishId $dishId already exist. Skipping fetch.")
                continue // Skip fetching if ingredients already exist
            }

            // Fetch ingredients and instructions if not already present
            Log.d("DishRepository", "Fetching ingredients and steps for dishId: $dishId")
            val response = withContext(Dispatchers.IO) {
                RetrofitInstance.spoonacularService.getRecipeInformation(dishId, apiKey)
            }

            val ingredients = response.extendedIngredients.map { ingredient ->
                IngredientEntity(
                    dishId = response.id,
                    name = ingredient.name,
                    amount = ingredient.amount,
                    unit = ingredient.unit
                )
            }

            withContext(Dispatchers.IO) {
                ingredientDao.insertIngredients(ingredients)
            }

            val steps = response.analyzedInstructions.flatMap { instruction ->
                instruction.steps.map { step ->
                    InstructionEntity(
                        dishId = response.id,
                        stepNumber = step.number,
                        description = step.step
                    )
                }
            }

            // Insert cooking steps in IO context
            withContext(Dispatchers.IO) {
                cookingStepDao.insertInstructions(steps)
            }
        }
    }

    // Get the count of dishes in the database
    suspend fun getDishCount(): Int {
        return dishDao.getDishCount()
    }

    // Get all dishes from the database
    suspend fun getAllDishes(): List<Recipe> {
        return withContext(Dispatchers.IO) {
            dishDao.getAllDishes()
        }
    }

    // Fetch ingredients for a specific dish
    suspend fun getIngredientsForDish(dishId: Int): List<IngredientEntity> {
        return withContext(Dispatchers.IO) {
            ingredientDao.getIngredientsForDish(dishId)
        }
    }

    // Fetch cooking steps for a specific dish
    suspend fun getCookingStepsForDish(dishId: Int): List<InstructionEntity> {
        return withContext(Dispatchers.IO) {
            cookingStepDao.getInstructionsForDish(dishId)
        }
    }

    suspend fun saveDishId(dishId: Long) {
        val savedDish = SavedDishEntity(dishId = dishId)
        withContext(Dispatchers.IO) {
            savedDishDao.insertSavedDish(savedDish)
        }
    }

    suspend fun isDishSaved(dishId: Long): Boolean {
        return savedDishDao.isDishSaved(dishId) > 0
    }
}
