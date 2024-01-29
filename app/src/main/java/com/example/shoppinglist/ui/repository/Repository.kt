package com.example.shoppinglist.ui.repository

import com.example.shoppinglist.data.room.ItemDao
import com.example.shoppinglist.data.room.ShoppingListDao
import com.example.shoppinglist.data.room.StoreDao
import com.example.shoppinglist.data.room.models.Item
import com.example.shoppinglist.data.room.models.ShoppingList
import com.example.shoppinglist.data.room.models.Store
import javax.inject.Inject

class Repository @Inject constructor(
    private val listDao: ShoppingListDao,
    private val itemDao: ItemDao,
    private val storeDao: StoreDao
) {
    val store = storeDao.getAllStores()
    val getItemsWithStoreAndList = listDao.getItemsWithStoreAndList()

    fun getItemWithStoreAndList(id: Int) = listDao
        .getItemWithStoreAndListFilteredById(id)

    fun getItemWithStoreAndListFilteredById(id: Int) = listDao
        .getItemsWithStoreAndListFilteredById(id)

    suspend fun insertList(shoppingList: ShoppingList){
        listDao.insertShoppingList(shoppingList)
    }

    suspend fun insertItem(item: Item){
        itemDao.insert(item)
    }

    suspend fun insertStore(store: Store){
        storeDao.insert(store)
    }

    suspend fun updateItem(item: Item){
        itemDao.update(item)
    }

    suspend fun deleteItem(item: Item){
        itemDao.delete(item)
    }
}