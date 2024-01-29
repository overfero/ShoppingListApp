package com.example.shoppinglist.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.data.room.ItemDao
import com.example.shoppinglist.data.room.ShoppingListDao
import com.example.shoppinglist.data.room.ShoppingListDatabase
import com.example.shoppinglist.data.room.StoreDao
import com.example.shoppinglist.ui.detail.DetailViewModel
import com.example.shoppinglist.ui.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.assisted.Assisted
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

object Graph{
    lateinit var db: ShoppingListDatabase
        private set

    val repository by lazy {
        Repository(
            listDao = db.listDao(),
            itemDao = db.itemDao(),
            storeDao = db.storeDao()
        )
    }

    fun provide(context: Context){
        db = ShoppingListDatabase.getDatabase(context)
    }
}