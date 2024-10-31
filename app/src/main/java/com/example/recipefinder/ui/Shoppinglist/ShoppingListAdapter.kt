package com.example.recipefinder.ui.Shoppinglist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.recipefinder.database.Shoppinglistitems.ShoppingListItem

import com.example.recipefinder.databinding.ItemShoppinglistBinding

class ShoppingListAdapter(
    private val context: Context,
    private var itemList: List<ShoppingListItem>,
    private val nothingFoundTextView: TextView,
    private val onDeleteClicked: (ShoppingListItem) -> Unit
) : RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder>() {
    private val baseUrl = "https://spoonacular.com/cdn/ingredients_100x100/"
    private var filteredItemList: List<ShoppingListItem> = itemList

    class ShoppingListViewHolder(val binding: ItemShoppinglistBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val binding = ItemShoppinglistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShoppingListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        if (position >= filteredItemList.size) return

        val item = filteredItemList[position]
        holder.binding.itemName.text = item.name
        holder.binding.itemName.isSelected = true
        Glide.with(holder.itemView.context)
            .load("$baseUrl${item.image}")
            .into(holder.binding.ivRecipe)
        holder.binding.ivDelete.setOnClickListener {

            AlertDialog.Builder(context)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete ${item.name}?")
                .setPositiveButton("Yes") { _, _ ->
                    onDeleteClicked(item)
                }
                .setNegativeButton("No", null)
                .show()
        }
    }


    override fun getItemCount(): Int = filteredItemList.size

    fun updateItems(newItems: List<ShoppingListItem>) {
        itemList = newItems
        filteredItemList = newItems
        notifyDataSetChanged()
        nothingFoundTextView.visibility = if (newItems.isEmpty()) View.VISIBLE else View.GONE
    }
}
