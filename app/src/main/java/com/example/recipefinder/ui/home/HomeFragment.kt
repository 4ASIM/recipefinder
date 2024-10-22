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

        // Set up RecyclerView in the fragment
        val recyclerView: RecyclerView = binding.recyclerView
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        val items = listOf(
            Pair(R.drawable.maindish, "Main Dish"),
            Pair(R.drawable.sidedish, "Side Dish"),
            Pair(R.drawable.appetizers, "Appetizerss"),
            Pair(R.drawable.breakfast, "BreakFast"),
            Pair(R.drawable.dessert, "Dessert"),
            Pair(R.drawable.salad, "Salad"),
            Pair(R.drawable.snacks, "Snack"),
            Pair(R.drawable.soup, "Soup")
        )

// Set adapter for RecyclerView
        val adapter = CardAdapter(items)
        recyclerView.adapter = adapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
