package com.example.recipefinder.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinder.R
import com.example.recipefinder.databinding.FragmentGalleryBinding
import com.example.recipefinder.ui.home.CardAdapter

class FavoriteFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(FavoriteViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root
                val recyclerView: RecyclerView = binding.rvFavorite
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

                val adapter = CardAdapter(items)
                recyclerView.adapter = adapter

                return root
            }

            override fun onDestroyView() {
                super.onDestroyView()
                _binding = null
            }
        }