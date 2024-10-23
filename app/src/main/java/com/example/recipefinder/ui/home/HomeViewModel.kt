package com.example.recipefinder.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipefinder.model.Recipe
import com.example.recipefinder.model.RecipeResponse
import com.example.recipefinder.model.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> get() = _recipes

    fun searchRecipes(query: String, ingredients: String) {
        RetrofitClient.instance.searchRecipes("", query, ingredients, 5)
            .enqueue(object : Callback<RecipeResponse> {
                override fun onResponse(
                    call: Call<RecipeResponse>,
                    response: Response<RecipeResponse>
                ) {
                    if (response.isSuccessful) {
                        _recipes.postValue(response.body()?.results ?: emptyList())
                    }
                }

                override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                    _recipes.postValue(emptyList())
                }
            })
    }
}
