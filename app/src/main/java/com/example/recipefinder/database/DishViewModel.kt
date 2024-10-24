package com.example.recipefinder.database
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class DishViewModel(private val repository: DishRepository) : ViewModel() {

    fun fetchDishes(cuisines: List<String>, apiKey: String) {
        viewModelScope.launch {
            repository.fetchAndStoreDishes(cuisines, apiKey)
        }
    }

    suspend fun getDishesByCuisine(cuisine: String): List<DishEntity> {
        return repository.getDishesByCuisine(cuisine)
    }
}
