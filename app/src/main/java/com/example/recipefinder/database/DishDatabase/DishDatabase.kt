package com.example.recipefinder.database.DishDatabase

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.recipefinder.database.IngredientDatabase.IngredientDao
import com.example.recipefinder.database.IngredientDatabase.IngredientEntity
import com.example.recipefinder.database.InstructionDatabase.InstructionDao
import com.example.recipefinder.database.InstructionDatabase.InstructionEntity
import com.example.recipefinder.database.MealPlan.MealPlan
import com.example.recipefinder.database.MealPlan.MealPlanDao
import com.example.recipefinder.database.Shoppinglistitems.ShoppingListDao
import com.example.recipefinder.database.Shoppinglistitems.ShoppingListItem
import com.example.recipefinder.database.favorite.SavedDishEntity
import com.example.recipefinder.database.favorite.SavedDishDao

@Database(entities = [DishEntity::class, IngredientEntity::class, InstructionEntity::class, SavedDishEntity::class, ShoppingListItem::class, MealPlan::class], version = 16)
abstract class DishDatabase : RoomDatabase() {
    abstract fun dishDao(): DishDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun instructionDao(): InstructionDao
    abstract fun savedDishDao(): SavedDishDao

    abstract fun shoppingListDao(): ShoppingListDao

    abstract fun mealPlanDao(): MealPlanDao

    companion object {
        @Volatile
        private var INSTANCE: DishDatabase? = null

        fun getDatabase(context: Context): DishDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DishDatabase::class.java,
                    "dish_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
