package com.example.shoppinglist.ui.home

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppinglist.data.room.ItemsWithStoreAndList
import com.example.shoppinglist.data.room.converters.formatDate
import com.example.shoppinglist.data.room.models.Item
import com.example.shoppinglist.ui.Utils
import com.example.shoppinglist.ui.theme.Purple200
import com.example.shoppinglist.ui.theme.Purple80
import com.example.shoppinglist.ui.theme.Shapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigate:(Int) -> Unit,
) {
    val homeViewModel = viewModel(modelClass = HomeViewModel::class.java)
    val homeState = homeViewModel.state
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigate.invoke(-1) }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
    ) {
        LazyColumn(modifier = Modifier.padding(it)){
            item {
                LazyRow(){
                    items(Utils.category){
                        CategoryItem(iconRes = it.resId,
                            title = it.title,
                            selected = it == homeState.category) {
                            homeViewModel.onCategoryChange(it)
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                }
            }
            items(homeState.items, key = { it.item.id }){
                val dismissState = rememberDismissState(
                    confirmValueChange = {value ->
                        if (value == DismissValue.DismissedToEnd || value == DismissValue.DismissedToStart){
                            homeViewModel.deleteItem(it.item)
                        }
                        true
                    }
                )
                SwipeToDismiss(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    state = dismissState,
                    background = {
                            Surface(modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp), color = MaterialTheme.colorScheme.surface) {
                                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = Color.Red,
                                        modifier = Modifier.fillMaxHeight().padding(start = 16.dp)
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = Color.Red,
                                        modifier = Modifier.fillMaxHeight().padding(end = 16.dp)
                                    )
                                }
                            }
                    },
                    dismissContent = {
                        ShoppingItem(
                            item = it,
                            isChecked = it.item.isChecked,
                            onItemClick = { onNavigate.invoke(it.item.id) },
                            onCheckedChange = homeViewModel::onItemCheckedChange
                        )
                    })
            }
        }
    }
}

@Composable
fun CategoryItem(
    @DrawableRes iconRes: Int,
    title: String,
    selected: Boolean,
    onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .selectable(
                selected = selected,
                interactionSource = MutableInteractionSource(),
                indication = rememberRipple(),
                onClick = { onItemClick.invoke() }
            ),
        border = BorderStroke(1.dp, if (selected) Color.LightGray else Color.Black),
        shape = Shapes.large,
        colors = CardDefaults.cardColors(containerColor = if (selected) Purple80.copy(alpha = 0.5f)
        else Purple80)
    ) {
        Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 4.dp, end = 8.dp)) {
            
            Icon(painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if (selected) Color.LightGray else Color.Black)
            Spacer(modifier = Modifier.size(4.dp))
            Text(text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = if (selected) Color.LightGray else Color.Black
                )
        }
    }
}

@Composable
fun ShoppingItem(
    item:ItemsWithStoreAndList,
    isChecked: Boolean,
    onItemClick: () -> Unit,
    onCheckedChange: (Item, Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onItemClick.invoke()
            }
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = item.item.itemName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(4.dp))
                
                Text(text = item.store.storeName)
                Spacer(modifier = Modifier.size(4.dp))

                Text(
                    text = formatDate(item.item.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                    )
            }

            Column(modifier = Modifier.padding(8.dp)){
                Text(
                    text = "Qty: ${item.item.qty}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(4.dp))

                Checkbox(
                    checked = isChecked,
                    onCheckedChange = {
                        onCheckedChange.invoke(item.item,it)
                    })
            }
        }
    }
}