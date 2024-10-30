package com.example.recipefinder.database.DishDatabase

import com.example.recipefinder.database.IngredientDatabase.IngredientDao
import com.example.recipefinder.database.IngredientDatabase.IngredientEntity
import com.example.recipefinder.database.InstructionDatabase.InstructionDao
import com.example.recipefinder.database.InstructionDatabase.InstructionEntity
import com.example.recipefinder.network.RetrofitInstance


import android.util.Log
import com.example.recipefinder.database.DishDatabase.DishDao
import com.example.recipefinder.database.DishDatabase.DishEntity
import com.example.recipefinder.database.Shoppinglistitems.ShoppingListDao
import com.example.recipefinder.database.Shoppinglistitems.ShoppingListItem
import com.example.recipefinder.database.favorite.SavedDishDao
import com.example.recipefinder.database.favorite.SavedDishEntity
import com.example.recipefinder.retrofit.Recipe

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.Flow
class DishRepository(
    private val dishDao: DishDao,
    private val ingredientDao: IngredientDao,
    private val cookingStepDao: InstructionDao,
    private val savedDishDao: SavedDishDao,
    val shoppingListDao: ShoppingListDao
) {
    private val apiKey = "9d9e99f4d79348318a0227e8886ba4ef"


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

    suspend fun fetchAndSaveIngredientsAndSteps() {
        val dishIds = withContext(Dispatchers.IO) {
            dishDao.getAllDishIds()
        }

        for (dishId in dishIds) {
            val existingIngredients = withContext(Dispatchers.IO) {
                ingredientDao.getIngredientsForDish(dishId)
            }

            if (existingIngredients.isNotEmpty()) {

                continue
            }


            val response = withContext(Dispatchers.IO) {
                RetrofitInstance.spoonacularService.getRecipeInformation(dishId, apiKey)
            }

            val ingredients = response.extendedIngredients.map { ingredient ->
                IngredientEntity(
                    dishId = response.id,
                    name = ingredient.name,
                    amount = ingredient.amount,
                    image = ingredient.image,
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

            withContext(Dispatchers.IO) {
                cookingStepDao.insertInstructions(steps)
            }
        }
    }

    suspend fun getDishCount(): Int {
        return dishDao.getDishCount()
    }

    suspend fun getAllDishes(): List<Recipe> {
        return withContext(Dispatchers.IO) {
            dishDao.getAllDishes()
        }
    }

    suspend fun getIngredientsForDish(dishId: Int): List<IngredientEntity> {
        return withContext(Dispatchers.IO) {
            ingredientDao.getIngredientsForDish(dishId)
        }
    }

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

    suspend fun addShoppingListItem(item: ShoppingListItem) {
        shoppingListDao.insert(item)
    }

    suspend fun getShoppingList(): List<ShoppingListItem> {
        return shoppingListDao.getAllItems()
    }

    suspend fun clearShoppingList() {
        shoppingListDao.clearShoppingList()
    }

    suspend fun isDishSaved(dishId: Long): Boolean {
        return savedDishDao.isDishSaved(dishId) > 0
    }

    fun getAllDishesFlow(): Flow<List<Recipe>> {
        return dishDao.getAllDishesFlow() // Ensure this returns a Flow in DishDao
    }
}

