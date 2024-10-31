package com.example.recipefinder.database.MealPlan
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MealPlanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateMealPlan(mealPlan: MealPlan)

    @Query("SELECT * FROM meal_plan_table WHERE date = :date AND mealTime = :mealTime LIMIT 1")
    suspend fun getMealPlan(date: String, mealTime: String): MealPlan?

    @Query("SELECT * FROM meal_plan_table WHERE date = :date")
    fun getMealPlansForDate(date: String): Flow<List<MealPlan>>
}
