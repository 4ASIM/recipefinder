package com.example.recipefinder.ui.home.detailsofincredient

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinder.R
import com.example.recipefinder.databinding.FragmentDetailBinding
import com.example.recipefinder.ui.home.CardAdapter

class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

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
        val horizontalAdapter = DetailAdapter(horizontalItems)
        recyclerView.adapter = horizontalAdapter

        return binding.root // Return the root view of the binding
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
