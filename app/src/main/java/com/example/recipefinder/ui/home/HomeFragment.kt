package com.example.recipefinder.ui.home

import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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

class HomeFragment : Fragment(),ItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var dishAdapter: DishAdapter
    private var job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)
    private val autoScrollHandler = Handler(Looper.getMainLooper())
    private var autoScrollRunnable: Runnable? = null
    private lateinit var recyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        val horizontalLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = horizontalLayoutManager
        // Data for horizontal RecyclerView
        val horizontalItems = listOf(
            Pair(R.drawable.maindish, "Mexican"),
            Pair(R.drawable.sidedish, "Chinese"),
            Pair(R.drawable.appetizers, "Italian"),
            Pair(R.drawable.breakfast, "Mexican"),
            Pair(R.drawable.dessert, "Japanese"),
            Pair(R.drawable.salad, "Pakistani"),
            Pair(R.drawable.snacks, "Indian"),
            Pair(R.drawable.soup, "Spanish")
        )

        // Set adapter for the horizontal RecyclerView
        val horizontalAdapter = CardAdapter(horizontalItems)
        recyclerView.adapter = horizontalAdapter
        startAutoScroll()

        val dishDao = DishDatabase.getDatabase(requireContext()).dishDao()
        val ingredientDao = DishDatabase.getDatabase(requireContext()).ingredientDao()
        val cookingStepDao = DishDatabase.getDatabase(requireContext()).instructionDao()
        val savedDishDao = DishDatabase.getDatabase(requireContext()).savedDishDao()
        val shoppingListDao = DishDatabase.getDatabase(requireContext()).shoppingListDao()

        val repository = DishRepository(dishDao, ingredientDao, cookingStepDao, savedDishDao, shoppingListDao)
        homeViewModel =
            ViewModelProvider(this, HomeViewModelFactory(repository)).get(HomeViewModel::class.java)

        val nothingFoundTextView = binding.noRecordsFound

        dishAdapter = DishAdapter(this,requireContext(),emptyList(), nothingFoundTextView)
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

    override fun onItemClick(position: Int, dish: Recipe) {
        Log.e("TAG", "onItemClick: $position ${dish.title}" )
        val bundle = Bundle().apply {
            putLong("dish_id", dish.id.toLong())
            putString("dish_name", dish.title)
            putString("dish_image", dish.image)
        }
        val navController = (context as AppCompatActivity).supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_dashboard)
            ?.findNavController()

        navController?.navigate(R.id.action_navigation_home_to_detailFragment, bundle)
    }
    private fun startAutoScroll() {
        autoScrollRunnable = Runnable {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
            val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
            val itemCount = recyclerView.adapter?.itemCount ?: 0

            // Scroll to the next position or wrap around to the beginning
            if (lastVisiblePosition < itemCount - 1) {
                recyclerView.smoothScrollToPosition(lastVisiblePosition + 1)
            } else {
                recyclerView.smoothScrollToPosition(0) // Start over if at the end
            }

            autoScrollHandler.postDelayed(autoScrollRunnable!!, 3000) // Adjust the delay as needed
        }
        autoScrollHandler.post(autoScrollRunnable!!)
    }

    private fun stopAutoScroll() {
        autoScrollHandler.removeCallbacks(autoScrollRunnable!!)
    }

    override fun onStop() {
        super.onStop()
        stopAutoScroll()
    }

    override fun onStart() {
        super.onStart()
        startAutoScroll()
    }
}

