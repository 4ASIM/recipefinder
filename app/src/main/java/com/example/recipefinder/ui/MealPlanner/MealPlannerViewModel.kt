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
                onDishAlreadyAdded()
            } else {
                repository.saveMealPlan(date, mealTime, dishId)
            }
        }
    }

    fun getMealPlansForDate(date: String) {
        viewModelScope.launch {
            repository.getMealPlansForDate(date).collect { mealPlanList ->
                val updatedMealPlans = mealPlanList.map { mealPlan ->
                    val dish = repository.getDishById(mealPlan.dishId)
                    mealPlan.dishName = dish?.title
                    mealPlan.dishImage = dish?.image
                    mealPlan
                }
                _mealPlans.postValue(updatedMealPlans)
            }
        }
    }
}
