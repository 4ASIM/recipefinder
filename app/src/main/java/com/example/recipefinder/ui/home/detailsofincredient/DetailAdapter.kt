package com.example.recipefinder.ui.home.detailsofincredient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinder.R

class DetailAdapter(private val items: List<Pair<Int, String>>) : RecyclerView.Adapter<DetailAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.card_image)
        private val nameTextView: TextView = itemView.findViewById(R.id.card_name)

        fun bind(item: Pair<Int, String>) {
            imageView.setImageResource(item.first)
            nameTextView.text = item.second
        }
    }
}
