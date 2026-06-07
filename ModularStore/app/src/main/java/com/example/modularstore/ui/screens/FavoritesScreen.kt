package com.example.modularstore.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.modularstore.ui.components.EmptyState
import com.example.modularstore.ui.components.ProductCard
import com.example.modularstore.ui.state.UiState
import com.example.modularstore.viewmodel.CategoryViewModel
import com.example.modularstore.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavController,
    productVm: ProductViewModel,
    categoryVm: CategoryViewModel
) {
    val favs    by productVm.favorites.collectAsStateWithLifecycle()
    val cart    by productVm.cart.collectAsStateWithLifecycle()
    val catState by categoryVm.uiState.collectAsStateWithLifecycle()
    val catMap  = when (val s = catState) {
        is UiState.Success -> s.data.associateBy { it.id }
        else -> emptyMap()
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Favoritos ❤️") }) }) { padding ->
        if (favs.isEmpty()) {
            EmptyState(Icons.Filled.Favorite, "Sin favoritos",
                "Toca el corazón en cualquier producto")
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(favs, key = { it.id }) { product ->
                    ProductCard(
                        product          = product,
                        categoryName     = catMap[product.categoryId]?.name ?: "—",
                        isFavorite       = true,
                        isInCart         = cart.any { it.id == product.id },
                        onViewDetail     = { navController.navigate("detail/${it.id}") },
                        onToggleFavorite = { productVm.toggleFavorite(it) },
                        onToggleCart     = { productVm.toggleCart(it) },
                        onEdit           = { navController.navigate("edit_product/${it.id}") },
                        onDelete         = { productVm.deleteProduct(it.id) }
                    )
                }
            }
        }
    }
}