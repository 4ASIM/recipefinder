package com.example.recipefinder.ui.Shoppinglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipefinder.database.Shoppinglistitems.ShoppingListDao
import com.example.recipefinder.database.Shoppinglistitems.ShoppingListItem
import kotlinx.coroutines.launch

class ShoppingListViewModel(
    private val shoppingListDao: ShoppingListDao
) : ViewModel() {

    private val _shoppingList = MutableLiveData<List<ShoppingListItem>>()
    val shoppingList: LiveData<List<ShoppingListItem>> get() = _shoppingList

    fun getShoppingListItems() {
        viewModelScope.launch {
            _shoppingList.value = shoppingListDao.getAllItems()
        }
    }

    fun deleteShoppingListItem(item: ShoppingListItem) {
        viewModelScope.launch {
            shoppingListDao.deleteItem(item.id)
            getShoppingListItems()
        }
    }
}
