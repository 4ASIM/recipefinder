package com.example.recipefinder.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipefinder.R
import com.example.recipefinder.databinding.HomescreenItemlayoutBinding
import com.example.recipefinder.model.Recipe

class IngredientAdapter(private var recipes: List<Recipe>) : RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {

    class IngredientViewHolder(val binding: HomescreenItemlayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val binding = HomescreenItemlayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IngredientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.binding.tvTitle.text = recipe.title


        Glide.with(holder.itemView.context)
            .load(recipe.image)
            .into(holder.binding.ivRecipe)

        holder.itemView.setOnClickListener {
            holder.itemView.findNavController().navigate(R.id.detailFragment)
        }

    }

    override fun getItemCount(): Int = recipes.size

    fun updateRecipes(newRecipes: List<Recipe>) {
        this.recipes = newRecipes
        notifyDataSetChanged()
    }
}
