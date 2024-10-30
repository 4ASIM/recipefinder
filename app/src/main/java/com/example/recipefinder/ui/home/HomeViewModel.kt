package com.example.recipefinder.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipefinder.database.DishDatabase.DishRepository
import com.example.recipefinder.retrofit.Recipe


import kotlinx.coroutines.launch

class HomeViewModel(private val repository: DishRepository) : ViewModel() {

    fun fetchAndSaveDishesIfNeeded(cuisines: List<String>) {
        viewModelScope.launch {
            val dishCount = repository.getDishCount()
            if (dishCount == 0) {
                repository.fetchDishes(cuisines)
            }
        }
    }
    fun fetchAndSaveIngredientsAndStepsIfNeeded() {
        viewModelScope.launch {
            repository.fetchAndSaveIngredientsAndSteps()
        }
    }

    suspend fun getAllDishes(): List<Recipe> {
        return repository.getAllDishes()
    }
}
