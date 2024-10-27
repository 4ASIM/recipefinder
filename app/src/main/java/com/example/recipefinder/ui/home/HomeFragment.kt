package com.example.recipefinder.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.recipefinder.database.DishDatabase.DishDatabase
import com.example.recipefinder.database.DishDatabase.DishRepository
import com.example.recipefinder.databinding.FragmentHomeBinding

import com.example.recipefinder.retrofit.Recipe

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var dishAdapter: DishAdapter // Declare dishAdapter here
    private var job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Initialize DAO instances
        val dishDao = DishDatabase.getDatabase(requireContext()).dishDao()
        val ingredientDao = DishDatabase.getDatabase(requireContext()).ingredientDao()
        val cookingStepDao = DishDatabase.getDatabase(requireContext()).instructionDao()

        // Initialize the repository and ViewModel
        val repository = DishRepository(dishDao, ingredientDao, cookingStepDao)
        homeViewModel =
            ViewModelProvider(this, HomeViewModelFactory(repository)).get(HomeViewModel::class.java)

        // Initialize RecyclerView and Adapter
        dishAdapter = DishAdapter(emptyList())
        binding.rvIngredent.adapter = dishAdapter
        binding.rvIngredent.layoutManager = LinearLayoutManager(context)

        // Fetch dishes and ingredients
        val cuisines = listOf("Italian", "Mexican", "Indian")
        fetchDishesAndIngredients(cuisines)

        return binding.root
    }

    private fun fetchDishesAndIngredients(cuisines: List<String>) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                homeViewModel.fetchAndSaveDishesIfNeeded(cuisines)
                homeViewModel.fetchAndSaveIngredientsAndStepsIfNeeded()
                val dishes = homeViewModel.getAllDishes()
                withContext(Dispatchers.Main) {
                    dishAdapter.updateDishes(dishes)
                }
            }
        }
    }
}
