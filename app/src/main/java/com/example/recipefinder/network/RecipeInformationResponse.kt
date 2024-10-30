package com.example.recipefinder.retrofit

data class RecipeInformationResponse(
    val id: Int,
    val title: String,
    val extendedIngredients: List<Ingredient>,
    val readyInMinutes: Int,
    val analyzedInstructions: List<Instruction>
)

data class Ingredient(
    val id: Int,
    val name: String,
    val amount: Double,
    val image: String,
    val unit: String
)

data class Instruction(
    val name: String,
    val steps: List<Step>
)

data class Step(
    val number: Int,
    val step: String
)
