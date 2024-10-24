package com.example.recipefinder.database

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.recipefinder.R
import kotlinx.coroutines.launch

class Dishactivity : AppCompatActivity() {
    private val dishViewModel: DishViewModel by viewModels {
        DishViewModelFactory((application as DishApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cuisines = listOf("Italian", "Chinese", "Mexican")
        val apiKey = "551d1c5bc8394bcd9543cdb5feb1bde2"

        dishViewModel.fetchDishes(cuisines, apiKey)

//        lifecycleScope.launch {
//            val dishes = dishViewModel.getDishesByCuisine("Italian")
//            // Do something with the result, e.g., update UI
//        }
    }
}