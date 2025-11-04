package com.example.assign5_3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.assign5_3.ui.theme.Assign5_3Theme
import kotlin.getValue
import kotlin.text.toIntOrNull

data class Location(val id: Int, val name: String, val description: String, val category: String)


class MainActivity : ComponentActivity() {
    private val viewModel: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assign5_3Theme {
                MainScreen(viewModel)
            }
        }
    }
}

sealed class Screen(val route: String, val title: String = "", val icon: ImageVector? = null) {
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Categories : Screen("categories", "Categories", Icons.Filled.Menu)
    object List : Screen("list/{category}") {
        fun createRoute(category: String) = "list/$category"
    }
    object Detail : Screen("detail/{locationId}") {
        fun createRoute(locationId: Int) = "detail/$locationId"
    }
}

val screens = listOf(
    Screen.Home,
    Screen.Categories
)

class MyViewModel : ViewModel() {
    val categories  = listOf("Museums", "Parks", "Restaurants")
    val locations = listOf(
        Location(1, "MIT Museum", "Technology museum in Cambridge", "Museums"),
        Location(2, "Boston Common", "Historic park in Boston", "Parks"),
        Location(3, "Legal Sea Foods", "Famous seafood restaurant", "Restaurants")
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MyViewModel){
    val navController = rememberNavController()

    Scaffold (
        topBar = {
            TopAppBar(title = {Text("Explore Boston", fontSize = 24.sp)})
        },
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route?.substringBefore('?') // normalize
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                screens.forEach { screen ->
                    NavigationBarItem(
                        label = { Text(screen.title) }, // The text label for the item.
                        icon = { screen.icon?.let { Icon(it, contentDescription = screen.title) } }, // The icon for the item.

                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,

                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true // Save the state of the screen you're leaving.
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ){
        innerPadding -> NavGraph(navController, viewModel, Modifier.padding(innerPadding))
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier){
    Column(modifier = modifier.padding(12.dp)){
        Text("Welcome to Boston!")
        Text("Explore Boston with our app!")
    }
}

@Composable
fun CategoriesScreen(viewModel: MyViewModel, onCategorySelected: (String) -> Unit, modifier: Modifier) {
    Column(modifier = modifier.padding(12.dp)){
        Text("Select a category:")
        LazyColumn() {
            items(viewModel.categories.size) { index ->
                Text(viewModel.categories[index], modifier.clickable { onCategorySelected(viewModel.categories[index]) })
            }
        }
    }
}

@Composable
fun ListScreen(category: String?, viewModel: MyViewModel, onLocationSelected: (Int) -> Unit, modifier: Modifier){
    val locations = viewModel.locations.filter { it.category == category }
    Column(modifier = modifier){
        Text("Locations in $category:")
        LazyColumn(modifier.padding(10.dp)) {
            items(locations.size) { index ->
                Text(locations[index].name, modifier.clickable { onLocationSelected(locations[index].id) })
            }
        }
    }
}

@Composable
fun DetailScreen(locationId: Int?, viewModel: MyViewModel, modifier: Modifier) {
    val location = viewModel.locations.find { it.id == locationId }

    Column(modifier){
        Text(location?.name ?: "Location not found", fontSize = 24.sp)
        Text(location?.description ?: "", fontSize = 16.sp)
        Text("Category: ${location?.category}", fontSize = 16.sp)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Assign5_3Theme {
        Greeting("Android")
    }
}