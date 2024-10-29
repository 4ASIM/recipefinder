package com.example.recipefinder.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipefinder.database.DishDatabase.DishDatabase
import com.example.recipefinder.database.DishDatabase.DishRepository
import com.example.recipefinder.databinding.FavouriteDashboardBinding
import com.example.recipefinder.retrofit.Recipe
import com.example.recipefinder.ui.home.DishAdapter

class FavouriteFragment : Fragment() {

    private var _binding: FavouriteDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FavouriteViewModel
    private lateinit var dishAdapter: DishssAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavouriteDashboardBinding.inflate(inflater, container, false)

        val dishDao = DishDatabase.getDatabase(requireContext()).dishDao()
        val ingredientDao = DishDatabase.getDatabase(requireContext()).ingredientDao()
        val cookingStepDao = DishDatabase.getDatabase(requireContext()).instructionDao()
        val savedDishDao = DishDatabase.getDatabase(requireContext()).savedDishDao()
        val shoppingListDao = DishDatabase.getDatabase(requireContext()).shoppingListDao()
        // Initialize the repository and ViewModel
        val repository = DishRepository(dishDao, ingredientDao, cookingStepDao, savedDishDao,shoppingListDao)

        viewModel = ViewModelProvider(this, FavouriteViewModelFactory(savedDishDao, dishDao)).get(FavouriteViewModel::class.java)

        // Initialize RecyclerView and Adapter
        dishAdapter = DishssAdapter(requireContext(), emptyList(), binding.noRecordsFound)
        binding.rvFavorite.adapter = dishAdapter
        binding.rvFavorite.layoutManager = LinearLayoutManager(context)

        // Fetch and display saved dishes
        fetchSavedDishes()

        return binding.root
    }

    private fun fetchSavedDishes() {
        viewModel.getSavedDishes { savedDishes ->
            dishAdapter.updateDishes(savedDishes)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}