package com.example.assign5_3

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun NavGraph(navController: NavHostController, viewModel: MyViewModel, modifier: Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route, // The first screen to show.
    ) {
        // Define a composable for each screen in our navigation graph.
        composable(Screen.Home.route) { HomeScreen(modifier) }
        composable(Screen.Categories.route) {
            CategoriesScreen(
                viewModel,
                {category -> navController.navigate(Screen.List.createRoute(category))},
                modifier
            )
        }
        composable(
            route = Screen.List.route,
            arguments = listOf(
                navArgument("category") { type = NavType.StringType }
            )
        ) {
            ListScreen(
                it.arguments?.getString("category"),
                viewModel,
                {locationId -> navController.navigate(Screen.Detail.createRoute(locationId))},
                modifier
            )
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("locationId") { type = NavType.IntType }
            )
        ) {
            DetailScreen(
                it.arguments?.getInt("locationId"),
                viewModel,
                modifier
            )
        }
    }
}