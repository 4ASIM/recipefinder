package com.example.recipefinder.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private val _savedDishes = MutableLiveData<List<Recipe>>()
    val savedDishes: LiveData<List<Recipe>> get() = _savedDishes

    fun getSavedDishes() {
        viewModelScope.launch {

            val savedDishIds = savedDishDao.getAllSavedDishIds()
            val savedDishes = savedDishIds.mapNotNull { dishId ->
                dishDao.getDishById(dishId)
            }
            _savedDishes.value = savedDishes
        }
    }
    fun deleteDish(dish: Recipe) {
        viewModelScope.launch {
            savedDishDao.deleteByDishId(dish.id.toLong())
            // Refresh the saved dishes list
            getSavedDishes()
        }
    }
}