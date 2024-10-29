package com.example.recipefinder.database.Shoppinglistitems

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShoppingListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ShoppingListItem)

    @Query("SELECT * FROM shopping_list WHERE name = :itemName AND amount = :amount AND unit = :unit")
    suspend fun getItem(itemName: String, amount: Double, unit: String): ShoppingListItem?

    @Query("SELECT * FROM shopping_list")
    suspend fun getAllItems(): List<ShoppingListItem>

    @Query("DELETE FROM shopping_list WHERE id = :itemId")
    suspend fun deleteItem(itemId: Long)

    @Query("DELETE FROM shopping_list")
    suspend fun clearShoppingList()
}

