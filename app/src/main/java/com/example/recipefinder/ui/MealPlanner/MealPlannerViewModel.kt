package com.example.recipefinder.ui.MealPlanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipefinder.database.DishDatabase.DishRepository
import com.example.recipefinder.retrofit.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import kotlinx.coroutines.flow.Flow

class MealPlannerViewModel(private val repository: DishRepository) : ViewModel() {

    // Fetches all dishes directly from the repository
    fun getAllDishes(): Flow<List<Recipe>> {
        return repository.getAllDishesFlow() // Use Flow for observing data changes
    }

    fun fetchAndSaveDishesIfNeeded(cuisines: List<String>) {
        viewModelScope.launch {
            val dishCount = repository.getDishCount()
            if (dishCount == 0) {
                repository.fetchDishes(cuisines)
            }
        }
    }
}
