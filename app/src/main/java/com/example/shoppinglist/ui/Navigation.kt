package com.example.shoppinglist.ui
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.shoppinglist.ui.detail.DetailScreen
import com.example.shoppinglist.ui.home.HomeScreen

enum class Route{
    Home,
    Detail
}

@Composable
fun ScreenNavigation(
    navHostController: NavHostController = rememberNavController()
) {
    NavHost(navController = navHostController, startDestination = Route.Home.name){
        composable(route = Route.Home.name){
            HomeScreen(onNavigate = { id ->
                navHostController.navigate(route = "${Route.Detail.name}?id=$id")
            })
        }

        composable(
            route = "${Route.Detail.name}?id={id}",
            arguments = listOf(navArgument("id"){type = NavType.IntType})
            ){
            val id = it.arguments?.getInt("id") ?: -1
            DetailScreen(id = id) {
                navHostController.navigateUp()
            }
        }
    }
}