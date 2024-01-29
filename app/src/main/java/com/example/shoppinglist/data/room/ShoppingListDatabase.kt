package com.example.shoppinglist.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.shoppinglist.data.room.converters.DateConverter
import com.example.shoppinglist.data.room.models.Item
import com.example.shoppinglist.data.room.models.ShoppingList
import com.example.shoppinglist.data.room.models.Store

@TypeConverters(value = [DateConverter::class])
@Database(
    entities = [ShoppingList::class, Item::class, Store::class],
    version = 1,
    exportSchema = false
)
abstract class ShoppingListDatabase: RoomDatabase() {
    abstract fun listDao(): ShoppingListDao
    abstract fun itemDao(): ItemDao
    abstract fun storeDao(): StoreDao

    companion object{
        @Volatile
        var INSTANCE: ShoppingListDatabase? = null
        fun getDatabase(context: Context): ShoppingListDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context,
                    ShoppingListDatabase::class.java,
                    "shopping_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}