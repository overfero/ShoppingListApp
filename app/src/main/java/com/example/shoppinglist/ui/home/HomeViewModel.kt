package com.example.shoppinglist.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.room.ItemsWithStoreAndList
import com.example.shoppinglist.data.room.models.Item
import com.example.shoppinglist.di.Graph
import com.example.shoppinglist.ui.Category
import com.example.shoppinglist.ui.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel(private val repository: Repository = Graph.repository): ViewModel(){
    var state by mutableStateOf(HomeState())
        private set

    init {
        getItems()
    }

    private fun getItems(){
        viewModelScope.launch {
            repository.getItemsWithStoreAndList.collectLatest {
                state = state.copy(items = it)
            }
        }
    }

    fun deleteItem(item: Item){
        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }

    fun onCategoryChange(category: Category){
        state = state.copy(category = category)
        filterBy(category.id)
    }

    fun onItemCheckedChange(item: Item, isChecked: Boolean){
        viewModelScope.launch {
            repository.updateItem(item = item.copy(isChecked = isChecked))
        }
    }

    fun filterBy(shoppingListId: Int){
        if (shoppingListId != 10001){
            viewModelScope.launch {
                repository.getItemWithStoreAndListFilteredById(shoppingListId)
                    .collectLatest { state = state.copy(items = it) }
            }
        }
    }
}

data class HomeState(
    val items: List<ItemsWithStoreAndList> = emptyList(),
    val category: Category = Category(),
    val itemChecked: Boolean = false
)