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

class IngredientAdapter(private val ingredients: List<IngredientEntity>) :
    RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {
    private val baseUrl = "https://spoonacular.com/cdn/ingredients_100x100/"
    inner class IngredientViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ingredientImage: ImageView = view.findViewById(R.id.ingredientImage)
        val ingredientName: TextView = view.findViewById(R.id.ingredientName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ingredient_item, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.ingredientName.text = ingredient.name.capitalizeWords()
        holder.ingredientName.isSelected = true
        Glide.with(holder.itemView.context)
            .load("$baseUrl${ingredient.image}")

            .into(holder.ingredientImage)
    }

    override fun getItemCount(): Int = ingredients.size
}

// Extension function to capitalize words
private fun String.capitalizeWords(): String = split(" ").joinToString(" ") { word ->
    word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}
