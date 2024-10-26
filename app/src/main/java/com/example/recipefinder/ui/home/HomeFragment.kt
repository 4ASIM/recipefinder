package com.example.recipefinder.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import com.example.recipefinder.database.DishDatabase.DishDatabase
import com.example.recipefinder.database.DishDatabase.DishRepository

import com.example.recipefinder.databinding.FragmentHomeBinding

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
//    private lateinit var gridAdapter: GridAdapter
    private var job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val dishDao = DishDatabase.getDatabase(requireContext()).dishDao()
        val ingredientDao = DishDatabase.getDatabase(requireContext()).ingredientDao()
        val cookingStepDao = DishDatabase.getDatabase(requireContext()).instructionDao()

        val repository = DishRepository(dishDao, ingredientDao, cookingStepDao)
        homeViewModel = ViewModelProvider(this, HomeViewModelFactory(repository)).get(HomeViewModel::class.java)

        val cuisines = listOf("Italian", "Mexican", "Indian")
        fetchDishesAndIngredients(cuisines)

        return binding.root
    }

    private fun fetchDishesAndIngredients(cuisines: List<String>) {
        coroutineScope.launch {
            val dishCount = withContext(Dispatchers.IO) {
                homeViewModel.getDishCount()
            }
            if (dishCount == 0) {
                withContext(Dispatchers.IO) {
                    homeViewModel.fetchAndSaveDishes(cuisines)
                }

                withContext(Dispatchers.IO) {
                    homeViewModel.fetchAndSaveIngredientsAndSteps()
                }
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}



