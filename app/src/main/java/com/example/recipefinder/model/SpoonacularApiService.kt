package com.example.recipefinder.model

import com.example.recipefinder.model.RecipeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SpoonacularApiService {
    @GET("recipes/complexSearch")
    fun searchRecipes(
        @Query("apiKey") apiKey: String,
        @Query("query") query: String,
        @Query("includeIngredients") ingredients: String,
        @Query("number") number: Int
    ): Call<RecipeResponse>
}
