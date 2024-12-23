package com.example.recipefinder.retrofit

data class RecipeResponse(
    val results: List<Recipe>
)

data class Recipe(
    val id: Int,
    val title: String,
    val maxReadyTime: Int?,
    val cuisine: String,
    val image: String
)

