package com.example.recipefinder.network

data class DishDetailResponse(
    val id: Int,
    val title: String,
    val image: String,
    val extendedIngredients: List<Ingredient>,
    val instructions: String
)

data class Ingredient(
    val name: String,
    val amount: Double,
    val unit: String
)
