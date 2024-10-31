package com.example.recipefinder.ui.MealPlanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipefinder.database.DishDatabase.DishRepository
import com.example.recipefinder.database.MealPlan.MealPlan
import com.example.recipefinder.retrofit.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import kotlinx.coroutines.flow.Flow

class MealPlannerViewModel(private val repository: DishRepository) : ViewModel() {

    private val _mealPlans = MutableLiveData<List<MealPlan>>()
    val mealPlans: LiveData<List<MealPlan>> get() = _mealPlans
    fun getAllDishes(): Flow<List<Recipe>> {
        return repository.getAllDishesFlow()
    }


    fun saveMealPlan(date: String, mealTime: String, dishId: Int, onDishAlreadyAdded: () -> Unit) {
        viewModelScope.launch {
            val isDishAdded = repository.isDishAddedForMeal(date, mealTime)
            if (isDishAdded > 0) {
                onDishAlreadyAdded() // Call the callback if the dish already exists
            } else {
                // Save the new meal plan entry
                repository.saveMealPlan(date, mealTime, dishId)
            }
        }
    }
//    fun fetchAndSaveDishesIfNeeded(cuisines: List<String>) {
//        viewModelScope.launch {
//            val dishCount = repository.getDishCount()
//            if (dishCount == 0) {
//                repository.fetchDishes(cuisines)
//            }
//        }
//    }

    fun getMealPlansForDate(date: String) {
        viewModelScope.launch {
            repository.getMealPlansForDate(date).collect { mealPlanList ->
                _mealPlans.postValue(mealPlanList)
            }
        }

    }
}
