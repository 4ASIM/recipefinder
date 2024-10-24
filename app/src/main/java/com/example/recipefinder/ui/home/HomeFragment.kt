package com.example.recipefinder.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinder.R
import com.example.recipefinder.databinding.FragmentHomeBinding
import com.example.recipefinder.ui.dashboard.FavouriteViewModel
import com.example.recipefinder.ui.dashboard.RecipeAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var IngredientAdapter: IngredientAdapter
    private val searchHandler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView: RecyclerView = binding.recyclerView
        val horizontalLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = horizontalLayoutManager

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

        val horizontalAdapter = CardAdapter(horizontalItems)
        recyclerView.adapter = horizontalAdapter

        IngredientAdapter = IngredientAdapter(listOf())
        binding.rvIngredent.layoutManager = LinearLayoutManager(context)
        binding.rvIngredent.adapter = IngredientAdapter
        viewModel.searchRecipes("", "")

        viewModel.recipes.observe(viewLifecycleOwner, Observer { recipes ->
            IngredientAdapter.updateRecipes(recipes)

            if (recipes.isEmpty()) {
                binding.noRecordsFound.visibility = View.VISIBLE
            } else {
                binding.noRecordsFound.visibility = View.GONE
            }
        })

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchRunnable?.let { searchHandler.removeCallbacks(it) }
                searchRunnable = Runnable {
                    newText?.let {
                        viewModel.searchRecipes(it, "")
                    }
                }
                searchHandler.postDelayed(searchRunnable!!, 300)
                return true
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        searchRunnable?.let { searchHandler.removeCallbacks(it) }
    }
}
