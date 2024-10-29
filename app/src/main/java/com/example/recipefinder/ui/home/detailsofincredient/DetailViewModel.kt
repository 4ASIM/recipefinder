package com.example.recipefinder.ui.home.detailsofincredient
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipefinder.database.DishDatabase.DishRepository
import com.example.recipefinder.database.IngredientDatabase.IngredientEntity
import com.example.recipefinder.database.InstructionDatabase.InstructionEntity
import com.example.recipefinder.database.Shoppinglistitems.ShoppingListItem
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

    fun saveShoppingListItem(ingredient: IngredientEntity) {
        val shoppingItem = ShoppingListItem(
            name = ingredient.name,
            amount = ingredient.amount,
            unit = ingredient.unit,
            image = ingredient.image
        )
        viewModelScope.launch(Dispatchers.IO) {
            // Check if the item already exists
            val existingItem = repository.shoppingListDao.getItem(ingredient.name, ingredient.amount, ingredient.unit)
            if (existingItem == null) {
                repository.addShoppingListItem(shoppingItem)
            }
        }
    }
}

