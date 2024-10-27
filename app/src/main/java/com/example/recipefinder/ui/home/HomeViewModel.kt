package com.example.recipefinder.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipefinder.database.DishDatabase.DishRepository
import com.example.recipefinder.retrofit.Recipe


import kotlinx.coroutines.launch

class HomeViewModel(private val repository: DishRepository) : ViewModel() {

    fun fetchAndSaveDishes(cuisines: List<String>) {
        viewModelScope.launch {
            repository.fetchDishes(cuisines)
        }
    }

    fun fetchAndSaveIngredientsAndSteps() {
        viewModelScope.launch {
            repository.fetchAndSaveIngredientsAndSteps()
        }
    }

    suspend fun getDishCount(): Int {
        return repository.getDishCount()
    }

    suspend fun getAllDishes(): List<Recipe> {
        return repository.getAllDishes()
    }
}