package com.example.recipefinder.database.DishDatabase

import com.example.recipefinder.database.IngredientDatabase.IngredientDao
import com.example.recipefinder.database.IngredientDatabase.IngredientEntity
import com.example.recipefinder.database.InstructionDatabase.InstructionDao
import com.example.recipefinder.database.InstructionDatabase.InstructionEntity
import com.example.recipefinder.network.Dish
import com.example.recipefinder.network.RetrofitInstance


import android.util.Log

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DishRepository(
    private val dishDao: DishDao,
    private val ingredientDao: IngredientDao,
    private val cookingStepDao: InstructionDao,
//    private val savedDishDao: SavedDishDao
) {
    private val apiKey = "b5229cc41ca94edcadecb61b1893c1dd"

    suspend fun fetchDishes(cuisines: List<String>) {
        val dishes = mutableListOf<DishEntity>()

        for (cuisine in cuisines) {
            val response = RetrofitInstance.spoonacularService.searchRecipes(cuisine, 5, apiKey)
            response.results.forEach { recipe ->
                dishes.add(DishEntity(recipe.id, recipe.title, recipe.image, cuisine))
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
            Log.d("DishRepository", "Fetched ingredients and steps for dishId: $dishId")
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


    suspend fun getDishCount(): Int {
        return dishDao.getDishCount()
    }

    suspend fun getAllDishes(): List<Dish> {
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

//    suspend fun saveDishId(dishId: Long) {
//        val savedDish = SavedDishEntity(dishId = dishId)
//        withContext(Dispatchers.IO) {
//            savedDishDao.insertSavedDish(savedDish)
//        }
//    }

//    suspend fun isDishSaved(dishId: Long): Boolean {
//        return savedDishDao.isDishSaved(dishId) > 0
//    }

}
