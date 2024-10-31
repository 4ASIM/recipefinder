package com.example.recipefinder.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipefinder.R
import com.example.recipefinder.databinding.HomescreenItemlayoutBinding
import com.example.recipefinder.databinding.ItemDishBinding
import com.example.recipefinder.retrofit.Recipe


class DishAdapter(var listener: ItemClickListener,private val context: Context, private var dishList: List<Recipe>, private val nothingFoundTextView: TextView) : RecyclerView.Adapter<DishAdapter.DishViewHolder>() {

    private var filteredDishList: List<Recipe> = dishList

    fun filter(query: String) {
        filteredDishList = if (query.isEmpty()) {
            dishList
        } else {
            dishList.filter {
                it.title.contains(query, ignoreCase = true) || it.cuisine.contains(query, ignoreCase = true)
            }
        }
        if (filteredDishList.isEmpty() && query.isNotEmpty()) {
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
        if (position >= filteredDishList.size) return

        val dish = filteredDishList[position]
        holder.binding.tvTitle.text = dish.title
        holder.binding.cusine.text = dish.cuisine
        holder.binding.tvTitle.isSelected = true
        Glide.with(holder.itemView.context)
            .load(dish.image)
            .into(holder.binding.ivRecipe)

        holder.itemView.setOnClickListener {
            holder.itemView.findNavController().navigate(R.id.detailFragment)
        }
        holder.itemView.setOnClickListener {

            listener.onItemClick(position,dish)
//
//            val bundle = Bundle().apply {
//                putLong("dish_id", dish.id.toLong())
//                putString("dish_name", dish.title)
//                putString("dish_image", dish.image)
//            }

//            val navController = (context as AppCompatActivity).supportFragmentManager
//                .findFragmentById(R.id.nav_host_fragment_activity_dashboard)
//                ?.findNavController()
//
//            navController?.navigate(R.id.action_navigation_home_to_detailFragment, bundle)
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

interface ItemClickListener{
    fun onItemClick(position: Int,dish: Recipe )
}
