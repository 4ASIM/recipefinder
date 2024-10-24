package com.example.recipefinder.database
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipefinder.database.DishRepository
import com.example.recipefinder.database.DishViewModel

class DishViewModelFactory(
    private val repository: DishRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DishViewModel::class.java)) {
            return DishViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
