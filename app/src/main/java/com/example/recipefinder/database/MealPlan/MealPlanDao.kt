package com.example.recipefinder.database.MealPlan
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MealPlanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealPlan(mealPlan: MealPlan)

    @Query("SELECT * FROM meal_plan_table WHERE date = :date AND mealTime = :mealTime LIMIT 1")
    suspend fun getMealPlanByDateAndMealTime(date: String, mealTime: String): MealPlan?

    @Query("SELECT COUNT(*) FROM meal_plan_table WHERE date = :date AND mealTime = :mealTime")
    suspend fun isDishAddedForMeal(date: String, mealTime: String): Int

//    @Query("SELECT * FROM meal_plan_table WHERE date = :date")
//    fun getMealPlansForDate(date: String): Flow<List<MealPlan>>

    @Query("SELECT * FROM meal_plan_table")
    suspend fun getAllMealPlans(): List<MealPlan>

    @Query("SELECT * FROM meal_plan_table WHERE date = :date")
    fun getMealPlansForDateFlow(date: String): Flow<List<MealPlan>>




}
