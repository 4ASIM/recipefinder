package com.example.recipefinder.database.InstructionDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface InstructionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInstructions(instructions: List<InstructionEntity>)

    @Query("SELECT * FROM instructions WHERE dishId = :dishId ORDER BY stepNumber")
    suspend fun getInstructionsForDish(dishId: Int): List<InstructionEntity>
}
