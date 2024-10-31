package com.example.recipefinder.ui.MealPlanner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
        holder.binding.apply {
            tvMealTime.text = mealPlan.date
            tvDishName.text = mealPlan.mealTime
        }
    }

    override fun getItemCount(): Int = mealPlans.size

    fun updateMealPlans(newMealPlans: List<MealPlan>) {
        mealPlans = newMealPlans
        notifyDataSetChanged()
    }
}
