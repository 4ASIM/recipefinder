package com.example.recipefinder.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipefinder.R
import com.example.recipefinder.databinding.HomescreenItemlayoutBinding
import com.example.recipefinder.databinding.ItemDishBinding
import com.example.recipefinder.retrofit.Recipe


class DishAdapter(private var dishList: List<Recipe>,private val nothingFoundTextView: TextView) : RecyclerView.Adapter<DishAdapter.DishViewHolder>() {

    private var filteredDishList: List<Recipe> = dishList


    // Update the displayed list based on the search query
    fun filter(query: String) {
        filteredDishList = if (query.isEmpty()) {
            dishList
        } else {
            dishList.filter {
                it.title.contains(query, ignoreCase = true) || it.cuisine.contains(query, ignoreCase = true)
            }
        }
        if (filteredDishList.isEmpty()) {
            nothingFoundTextView.visibility = View.VISIBLE
        } else {
            nothingFoundTextView.visibility = View.GONE
        }
        notifyDataSetChanged()
    }

    class DishViewHolder(val binding: HomescreenItemlayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishAdapter.DishViewHolder {
        val binding = HomescreenItemlayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DishViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        if (position >= filteredDishList.size) return  // Prevent index out of bounds

        val dish = filteredDishList[position]
        holder.binding.tvTitle.text = dish.title
        holder.binding.cusine.text = dish.cuisine
        holder.binding.tvTime.text = dish.maxReadyTime.toString()

        Glide.with(holder.itemView.context)
            .load(dish.image)
            .into(holder.binding.ivRecipe)

        holder.itemView.setOnClickListener {
            holder.itemView.findNavController().navigate(R.id.detailFragment)
        }
    }

    override fun getItemCount(): Int {
        return filteredDishList.size  // Use filteredDishList for item count
    }

    // Method to update the list of dishes and notify the adapter
    fun updateDishes(newDishes: List<Recipe>) {
        dishList = newDishes
        filteredDishList = newDishes  // Update filtered list as well
        notifyDataSetChanged()
    }
}
