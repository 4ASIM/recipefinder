package com.example.recipefinder.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipefinder.database.DishDatabase.DishDao
import com.example.recipefinder.database.favorite.SavedDishDao
import com.example.recipefinder.retrofit.Recipe
import kotlinx.coroutines.launch

class FavouriteViewModel(
    private val savedDishDao: SavedDishDao,
    private val dishDao: DishDao
) : ViewModel() {


    fun getSavedDishes(onResult: (List<Recipe>) -> Unit) {
        viewModelScope.launch {
            // Get all saved dish IDs
            val savedDishIds = savedDishDao.getAllSavedDishIds() // Assuming you have this method in your SavedDishDao
            val savedDishes = savedDishIds.mapNotNull { dishId ->
                dishDao.getDishById(dishId) // Assuming you have a method to get a dish by ID
            }
            onResult(savedDishes)
        }
    }
}