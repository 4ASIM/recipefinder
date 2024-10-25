package com.example.recipefinder.database

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SpoonacularApi {
    @GET("recipes/complexSearch")
    fun getDishes(
        @Query("cuisine") cuisine: String,
        @Query("number") number: Int = 5,
        @Query("apiKey") apiKey: String
    ): Call<DishResponse>
}

data class DishResponse(
    val results: List<Dish>
)

data class Dish(
    val id: Int,
    val title: String,
    val image: String
)