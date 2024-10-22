package com.example.recipefinder.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinder.R
import com.example.recipefinder.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up RecyclerView for the horizontal items
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

        // Set up RecyclerView for the vertical ingredients
        val rvIngredients: RecyclerView = binding.rvIngredent
        val verticalLayoutManager = LinearLayoutManager(context)
        rvIngredients.layoutManager = verticalLayoutManager

        // Data for vertical RecyclerView (ingredients)
        val verticalItems = listOf(
            Pair(R.drawable.sidedish, "Ingredient 1"),
            Pair(R.drawable.sidedish, "Ingredient 2"),
            Pair(R.drawable.sidedish, "Ingredient 3"),
            Pair(R.drawable.sidedish, "Ingredient 4"),
            Pair(R.drawable.sidedish, "Ingredient 5")
        )

        // Set adapter for the vertical RecyclerView
        val verticalAdapter = IngredientAdapter(verticalItems)
        rvIngredients.adapter = verticalAdapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
