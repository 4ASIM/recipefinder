package com.example.recipefinder.database

import android.util.Log
import retrofit2.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DishRepository(private val dishDao: DishDao) {

    suspend fun fetchAndStoreDishes(cuisines: List<String>, apiKey: String) {
        for (cuisine in cuisines) {
            try {

                val response = RetrofitInstance.api.getDishes(cuisine, 5, apiKey).await()

                if (response.results.isNotEmpty()) {

                    val dishes = response.results.map {
                        DishEntity(it.id, it.title, it.image, cuisine)
                    }
                    withContext(Dispatchers.IO) {
                        dishDao.insertDishes(dishes)
                    }
                }
            } catch (e: Exception) {
                Log.e("DishRepository", "Error fetching dishes for $cuisine", e)
            }
        }
    }

    suspend fun getDishesByCuisine(cuisine: String): List<DishEntity> {
        return withContext(Dispatchers.IO) {
            dishDao.getDishesByCuisine(cuisine)
        }
    }
}