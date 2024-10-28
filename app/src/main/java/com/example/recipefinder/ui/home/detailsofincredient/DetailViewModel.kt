package com.example.recipefinder.ui.home.detailsofincredient
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipefinder.database.DishDatabase.DishRepository
import com.example.recipefinder.database.IngredientDatabase.IngredientEntity
import com.example.recipefinder.database.InstructionDatabase.InstructionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailViewModel(private val repository: DishRepository) : ViewModel() {

    fun fetchIngredients(dishId: Int, callback: (List<IngredientEntity>) -> Unit) {
        viewModelScope.launch {
            val ingredients = repository.getIngredientsForDish(dishId)
            callback(ingredients)
        }
    }

    fun fetchInstructionSteps(dishId: Int, callback: (List<InstructionEntity>) -> Unit) {
        viewModelScope.launch {
            val instructionSteps = repository.getCookingStepsForDish(dishId)
            callback(instructionSteps)
        }
    }

    fun saveDishId(dishId: Long) {
        viewModelScope.launch {
            repository.saveDishId(dishId)
        }
    }
    suspend fun isDishSaved(dishId: Long): Boolean {
        return withContext(Dispatchers.IO) {
            repository.isDishSaved(dishId)
        }
    }

}
