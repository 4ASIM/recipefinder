package com.example.recipefinder.ui.MealPlanner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipefinder.database.MealPlan.MealPlan
import com.example.recipefinder.databinding.ItemMealplannerBinding

class MealPlanAdapter(private var mealPlans: List<MealPlan>) : RecyclerView.Adapter<MealPlanAdapter.MealPlanViewHolder>() {

    class MealPlanViewHolder(val binding: ItemMealplannerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealPlanViewHolder {
        val binding = ItemMealplannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealPlanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealPlanViewHolder, position: Int) {
        val mealPlan = mealPlans[position]
        holder.binding.tvMealTime.isSelected = true
        holder.binding.apply {
            tvMealTime.text = mealPlan.mealTime
            tvDishName.text = mealPlan.dishName

            if (mealPlan.dishImage != null) {

                Glide.with(holder.itemView.context)
                    .load(mealPlan.dishImage)
                    .into(ivDishImage)
            }
        }

    }

    override fun getItemCount(): Int = mealPlans.size

    fun updateMealPlans(newMealPlans: List<MealPlan>) {
        mealPlans = newMealPlans
        notifyDataSetChanged()
    }
}
