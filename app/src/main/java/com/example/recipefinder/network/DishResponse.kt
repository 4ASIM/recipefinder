package com.example.recipefinder.network

data class DishResponse(
    val results: List<Dish>
)

data class Dish(
    val id: Int,
    val title: String,
    val image: String
)