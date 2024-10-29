package com.example.recipefinder.ui.home.detailsofincredient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipefinder.R
import com.example.recipefinder.database.IngredientDatabase.IngredientEntity
import com.example.recipefinder.database.Shoppinglistitems.ShoppingListItem

class ShoppingListAdapter(private val ingredients: List<IngredientEntity>) :
    RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder>() {

    private val baseUrl = "https://spoonacular.com/cdn/ingredients_100x100/"

    inner class ShoppingListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ingredientImage: ImageView = view.findViewById(R.id.ingredientImage)
        val ingredientName: TextView = view.findViewById(R.id.ingredientName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ingredient_item, parent, false)
        return ShoppingListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.ingredientName.text = ingredient.name
        Glide.with(holder.itemView.context)
            .load("$baseUrl${ingredient.image}")
            .into(holder.ingredientImage)
    }

    override fun getItemCount(): Int = ingredients.size
}
