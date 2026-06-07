package com.example.modularstore.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.modularstore.data.repository.CategoryRepositoryImpl
import com.example.modularstore.data.repository.ProductRepositoryImpl
import com.example.modularstore.ui.screens.*
import com.example.modularstore.viewmodel.*

@Composable
fun AppNavigation() {
    val productVm: ProductViewModel = viewModel(
        factory = ProductViewModelFactory(ProductRepositoryImpl())
    )
    val categoryVm: CategoryViewModel = viewModel(
        factory = CategoryViewModelFactory(CategoryRepositoryImpl())
    )

    val navController = rememberNavController()
    val cartItems by productVm.cart.collectAsStateWithLifecycle()
    val favItems  by productVm.favorites.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            val current by navController.currentBackStackEntryAsState()
            val route   = current?.destination?.route
            val showBar = route in listOf("home", "favorites", "cart")

            if (showBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = route == "home",
                        onClick  = { navController.navigate("home") { launchSingleTop = true } },
                        icon     = { Icon(Icons.Filled.Home, "Inicio") },
                        label    = { Text("Inicio") }
                    )
                    NavigationBarItem(
                        selected = route == "favorites",
                        onClick  = { navController.navigate("favorites") { launchSingleTop = true } },
                        icon = {
                            BadgedBox(badge = {
                                if (favItems.isNotEmpty())
                                    Badge { Text(favItems.size.toString()) }
                            }) { Icon(Icons.Filled.Favorite, "Favoritos") }
                        },
                        label = { Text("Favoritos") }
                    )
                    NavigationBarItem(
                        selected = route == "cart",
                        onClick  = { navController.navigate("cart") { launchSingleTop = true } },
                        icon = {
                            BadgedBox(badge = {
                                if (cartItems.isNotEmpty())
                                    Badge { Text(cartItems.size.toString()) }
                            }) { Icon(Icons.Filled.ShoppingCart, "Carrito") }
                        },
                        label = { Text("Carrito") }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = "splash",
            modifier         = Modifier.padding(innerPadding)
        ) {
            composable("splash") {
                SplashScreen(onFinish = {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                })
            }
            composable("home") {
                HomeScreen(navController, productVm, categoryVm)
            }
            composable("categories") {
                CategoryScreen(navController, categoryVm)
            }
            composable("add_product") {
                AddEditProductScreen(navController, productVm, categoryVm)
            }
            composable("edit_product/{productId}") { back ->
                val id = back.arguments?.getString("productId")?.toIntOrNull() ?: return@composable
                AddEditProductScreen(navController, productVm, categoryVm, productId = id)
            }
            composable("detail/{productId}") { back ->
                val id = back.arguments?.getString("productId")?.toIntOrNull() ?: return@composable
                ProductDetailScreen(id, navController, productVm, categoryVm)
            }
            composable("favorites") {
                FavoritesScreen(navController, productVm, categoryVm)
            }
            composable("cart") {
                CartScreen(productVm)
            }
        }
    }
}