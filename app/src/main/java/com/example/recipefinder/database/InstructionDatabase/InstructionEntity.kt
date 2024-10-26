package com.example.recipefinder.database.InstructionDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "instructions")
data class InstructionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dishId: Int,
    val stepNumber: Int,
    val description: String
)
