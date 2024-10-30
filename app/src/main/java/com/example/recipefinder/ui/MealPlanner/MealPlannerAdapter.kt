package com.example.recipefinder.ui.MealPlanner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipefinder.databinding.ItemDishBinding
import com.example.recipefinder.retrofit.Recipe

class MealPlannerAdapter(
    private var dishes: List<Recipe>
) : RecyclerView.Adapter<MealPlannerAdapter.MealPlannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealPlannerViewHolder {
        val binding = ItemDishBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealPlannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealPlannerViewHolder, position: Int) {
        val dish = dishes[position]
        holder.binding.dishName.text = dish.title
        Glide.with(holder.itemView.context)
            .load(dish.image)
            .into(holder.binding.dishImage)
    }

    override fun getItemCount(): Int = dishes.size

    fun updateDishes(newDishes: List<Recipe>) {
        dishes = newDishes
        notifyDataSetChanged()
    }

    class MealPlannerViewHolder(val binding: ItemDishBinding) : RecyclerView.ViewHolder(binding.root)
}
