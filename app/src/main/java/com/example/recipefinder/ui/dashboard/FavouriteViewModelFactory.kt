package com.example.recipefinder.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipefinder.database.DishDatabase.DishDao
import com.example.recipefinder.database.favorite.SavedDishDao

class FavouriteViewModelFactory(
    private val savedDishDao: SavedDishDao,
    private val dishDao: DishDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavouriteViewModel::class.java)) {
            return FavouriteViewModel(savedDishDao, dishDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
