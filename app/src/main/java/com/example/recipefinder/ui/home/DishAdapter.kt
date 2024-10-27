package com.example.recipefinder.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipefinder.R
import com.example.recipefinder.databinding.HomescreenItemlayoutBinding
import com.example.recipefinder.databinding.ItemDishBinding
import com.example.recipefinder.retrofit.Recipe


class DishAdapter(private var dishes: List<Recipe>) : RecyclerView.Adapter<DishAdapter.DishViewHolder>() {

    class DishViewHolder(val binding: HomescreenItemlayoutBinding) : RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishAdapter.DishViewHolder {
        val binding = HomescreenItemlayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DishAdapter.DishViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DishAdapter.DishViewHolder, position: Int) {
        val recipe = dishes[position]
        holder.binding.tvTitle.text = recipe.title
        holder.binding.cusine.text = recipe.cuisine
        holder.binding.tvTime.text = recipe.maxReadyTime.toString()



        Glide.with(holder.itemView.context)
            .load(recipe.image)
            .into(holder.binding.ivRecipe)

        holder.itemView.setOnClickListener {
            holder.itemView.findNavController().navigate(R.id.detailFragment)
        }

    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    // Method to update the list of dishes and notify the adapter
    fun updateDishes(newDishes: List<Recipe>) {
        dishes = newDishes
        notifyDataSetChanged()
    }
}
