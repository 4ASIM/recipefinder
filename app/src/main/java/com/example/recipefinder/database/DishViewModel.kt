//package com.example.recipefinder.database
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.recipefinder.database.DishDatabase.DishEntity
//import com.example.recipefinder.database.DishDatabase.DishRepository
//import com.example.recipefinder.database.IngredientDatabase.IngredientEntity
//import com.example.recipefinder.database.InstructionDatabase.InstructionEntity
//import kotlinx.coroutines.launch
//
//
//class DishViewModel(private val repository: DishRepository) : ViewModel() {
//
//    fun fetchDishes(cuisines: List<String>, apiKey: String) {
//        viewModelScope.launch {
//            repository.fetchAndStoreDishes(cuisines, apiKey)
//        }
//    }
//
//    suspend fun getDishesByCuisine(cuisine: String): List<DishEntity> {
//        return repository.getDishesByCuisine(cuisine)
//    }
//
//    suspend fun getIngredientsForDish(dishId: Int): List<IngredientEntity> {
//        return repository.getIngredientsForDish(dishId)
//    }
//
//    suspend fun getInstructionsForDish(dishId: Int): List<InstructionEntity> {
//        return repository.getInstructionsForDish(dishId)
//    }
//}
