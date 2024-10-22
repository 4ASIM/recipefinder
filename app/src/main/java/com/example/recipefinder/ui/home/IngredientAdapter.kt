package com.example.recipefinder.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinder.R
import com.example.recipefinder.databinding.HomescreenItemlayoutBinding

class IngredientAdapter(private val items: List<Pair<Int, String>>) : RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: HomescreenItemlayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Pair<Int, String>) {
            binding.ivRecipe.setImageResource(item.first)
            binding.tvTitle.text = item.second

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HomescreenItemlayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
