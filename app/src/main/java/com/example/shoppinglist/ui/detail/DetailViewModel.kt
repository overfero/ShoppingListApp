package com.example.shoppinglist.ui.detail

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.room.models.Item
import com.example.shoppinglist.data.room.models.ShoppingList
import com.example.shoppinglist.data.room.models.Store
import com.example.shoppinglist.di.Graph
import com.example.shoppinglist.ui.Category
import com.example.shoppinglist.ui.Utils
import com.example.shoppinglist.ui.repository.Repository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

class DetailViewModel(
    private val repository: Repository = Graph.repository,
    private val itemId: Int
) : ViewModel() {
    var state by mutableStateOf(DetailState())
    val isFieldsNotEmpty: Boolean
        get() = state.item.isNotEmpty() &&
                state.store.isNotEmpty() &&
                state.qty.isNotEmpty()

    init {
        addListItem()
        getStore()
        if (itemId != -1){
            viewModelScope.launch {
                repository
                    .getItemWithStoreAndList(itemId)
                    .collectLatest {
                        state = state.copy(
                            item = it.item.itemName,
                            store = it.store.storeName,
                            date = it.item.date,
                            category = Utils.category.find {c ->
                                c.id == it.shoppingList.id
                            } ?: Category(),
                            qty = it.item.qty
                        )
                    }
            }
        }
    }

    init {
        state = if (itemId != -1){
            state.copy(isUpdatingItem = true)
        }else {
            state.copy(isUpdatingItem = false)
        }
    }

    fun onItemChange(newValue: String){
        state = state.copy(item = newValue)
    }

    fun onStoreChange(newValue: String){
        state = state.copy(store = newValue)
    }

    fun onQtyChange(newValue: String){
        state = state.copy(qty = newValue)
    }

    fun onDateChange(newValue: Date){
        state = state.copy(date = newValue)
    }

    fun onCategoryChange(newValue: Category){
        state = state.copy(category = newValue)
    }

    fun onScreenDialogDissmised(newValue: Boolean){
        state = state.copy(isScreenDialogDismissed = newValue)
    }

    fun addShoppingItem(){
        viewModelScope.launch {
            repository.insertItem(
                Item(
                    itemName = state.item,
                    qty = state.qty,
                    listId = state.category.id,
                    date = state.date,
                    isChecked = false,
                    storeIdFk = state.storeList.find {
                        it.storeName == state.store
                    }?.id ?: 0
                )
            )
        }
    }

    fun addListItem(){
        viewModelScope.launch {
            Utils.category.forEach {
                repository.insertList(
                    ShoppingList(
                        id = it.id,
                        name = it.title
                    )
                )
            }
        }
    }

    fun updateShoppingItem(id: Int){
        viewModelScope.launch {
            repository.insertItem(
                Item(
                    itemName = state.item,
                    qty = state.qty,
                    listId = state.category.id,
                    date = state.date,
                    isChecked = false,
                    storeIdFk = state.storeList.find {
                        it.storeName == state.store
                    }?.id ?: 0,
                    id = id
                )
            )
        }
    }

    fun addStore(){
        viewModelScope.launch {
            repository.insertStore(
                Store(
                    listIdFk = state.category.id,
                    storeName = state.store
                )
            )
        }
    }

    fun getStore(){
        viewModelScope.launch {
            repository.store.collectLatest {
                state = state.copy(storeList = it)
            }
        }
    }
}

data class DetailState(
    val storeList: List<Store> = emptyList(),
    val item: String = "",
    val store: String = "",
    val date: Date = Date(),
    val qty: String = "",
    val isScreenDialogDismissed: Boolean = true,
    val isUpdatingItem: Boolean = false,
    val category: Category = Category()
)

@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(private val itemId: Int): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(itemId = itemId) as T
    }
}