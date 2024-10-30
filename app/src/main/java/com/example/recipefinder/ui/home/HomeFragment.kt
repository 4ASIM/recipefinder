package com.example.recipefinder.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinder.R

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
    private lateinit var dishAdapter: DishAdapter
    private var job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val recyclerView: RecyclerView = binding.recyclerView
        val horizontalLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = horizontalLayoutManager

        // Data for horizontal RecyclerView
        val horizontalItems = listOf(
            Pair(R.drawable.maindish, "Main Dish"),
            Pair(R.drawable.sidedish, "Side Dish"),
            Pair(R.drawable.appetizers, "Appetizers"),
            Pair(R.drawable.breakfast, "BreakFast"),
            Pair(R.drawable.dessert, "Dessert"),
            Pair(R.drawable.salad, "Salad"),
            Pair(R.drawable.snacks, "Snack"),
            Pair(R.drawable.soup, "Soup")
        )

        // Set adapter for the horizontal RecyclerView
        val horizontalAdapter = CardAdapter(horizontalItems)
        recyclerView.adapter = horizontalAdapter

        val dishDao = DishDatabase.getDatabase(requireContext()).dishDao()
        val ingredientDao = DishDatabase.getDatabase(requireContext()).ingredientDao()
        val cookingStepDao = DishDatabase.getDatabase(requireContext()).instructionDao()
        val savedDishDao = DishDatabase.getDatabase(requireContext()).savedDishDao()
        val shoppingListDao = DishDatabase.getDatabase(requireContext()).shoppingListDao()

        val repository = DishRepository(dishDao, ingredientDao, cookingStepDao, savedDishDao, shoppingListDao)
        homeViewModel =
            ViewModelProvider(this, HomeViewModelFactory(repository)).get(HomeViewModel::class.java)

        val nothingFoundTextView = binding.noRecordsFound

        dishAdapter = DishAdapter(requireContext(),emptyList(), nothingFoundTextView)
        binding.rvIngredent.adapter = dishAdapter
        binding.rvIngredent.layoutManager = LinearLayoutManager(context)

        val cuisines = listOf("Italian", "Mexican", "Indian")
        fetchDishesAndIngredients(cuisines)

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                dishAdapter.filter(newText ?: "")
                return true
            }
        })

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
