package com.example.recipefinder.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipefinder.R
import com.example.recipefinder.databinding.HomescreenItemlayoutBinding
import com.example.recipefinder.databinding.ItemDishBinding
import com.example.recipefinder.databinding.ItemFavouriteBinding
import com.example.recipefinder.retrofit.Recipe


class DishssAdapter(private val context: Context, private var dishList: List<Recipe>, private val nothingFoundTextView: TextView,private val onDeleteClicked: (Recipe) -> Unit) : RecyclerView.Adapter<DishssAdapter.DishssViewHolder>() {

    private var filteredDishList: List<Recipe> = dishList


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

    class DishssViewHolder(val binding: ItemFavouriteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishssAdapter.DishssViewHolder {
        val binding =  ItemFavouriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DishssViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DishssViewHolder, position: Int) {
        if (position >= filteredDishList.size) return  // Prevent index out of bounds

        val dish = filteredDishList[position]
        holder.binding.tvTitle.text = dish.title
        holder.binding.cusine.text = dish.cuisine
        holder.binding.tvTitle.isSelected = true

        Glide.with(holder.itemView.context)
            .load(dish.image)
            .into(holder.binding.ivRecipe)
        holder.binding.ivDelete.setOnClickListener {
            onDeleteClicked(dish) // Call the callback with the dish to delete
        }
        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putLong("dish_id", dish.id.toLong())
                putString("dish_name", dish.title)
                putString("dish_image", dish.image)
            }

            val navController = (context as AppCompatActivity).supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment_activity_dashboard)
                ?.findNavController()

            navController?.navigate(R.id.detailFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return filteredDishList.size
    }

    fun updateDishes(newDishes: List<Recipe>) {
        dishList = newDishes
        filteredDishList = newDishes
        notifyDataSetChanged()
    }
}