package com.example.shoppinglist.data.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "shopping_list")
data class ShoppingList(
    @PrimaryKey
    @ColumnInfo(name = "list_id")
    val id: Int,
    val name: String
)

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    val id: Int = 0,
    val itemName: String,
    val qty: String,
    val listId: Int,
    val storeIdFk: Int,
    val date: Date,
    val isChecked: Boolean
)

@Entity(tableName = "stores")
data class Store(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "store_id")
    val id: Int = 0,
    val listIdFk: Int,
    val storeName: String
)