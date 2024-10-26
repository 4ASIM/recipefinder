//package com.example.recipefinder.database
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import androidx.activity.viewModels
//import com.example.recipefinder.R
//import com.example.recipefinder.database.DishDatabase.DishApplication
//
//class Dishactivity : AppCompatActivity() {
//    private val dishViewModel: DishViewModel by viewModels {
//        DishViewModelFactory((application as DishApplication).repository)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val cuisines = listOf("Italian", "Chinese", "Mexican")
//        val apiKey = "5594f5c6b1834c6da0b0db02bd5c3b82"
//
//
//        dishViewModel.fetchDishes(cuisines, apiKey)
//
//
////        lifecycleScope.launch {
////            try {
////                val dishes = dishViewModel.getDishesByCuisine("Italian")
////                if (dishes.isNotEmpty()) {
////
////                    val dishId = dishes[0].id
////                    val ingredients = dishViewModel.getIngredientsForDish(dishId)
////                    val instructions = dishViewModel.getInstructionsForDish(dishId)
////
////
////                }
////            } catch (e: Exception) {
////                Log.e("DishActivity", "Error fetching data", e)
////            }
////        }
//    }
//}
//
//
//
//
