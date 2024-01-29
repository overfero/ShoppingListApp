package com.example.shoppinglist

import android.app.Application
import com.example.shoppinglist.di.Graph
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ShoppingListApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}