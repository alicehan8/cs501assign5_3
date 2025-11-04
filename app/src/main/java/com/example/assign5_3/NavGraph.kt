package com.example.assign5_3

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavGraph(navController: NavHostController, viewModel: MyViewModel, modifier: Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route, // The first screen to show.
    ) {
        // Define a composable for each screen in our navigation graph.
        composable(Screen.Home.route) { HomeScreen(viewModel) }
        composable(Screen.Categories.route) { CategoriesScreen(viewModel) }
        composable(Screen.List.route) { Text("List") }
    }
}