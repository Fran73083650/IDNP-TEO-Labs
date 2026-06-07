package com.example.modularstore.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.modularstore.ui.components.*
import com.example.modularstore.viewmodel.CategoryViewModel
import com.example.modularstore.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: Int,
    navController: NavController,
    productVm: ProductViewModel,
    categoryVm: CategoryViewModel
) {
    val product  = productVm.getById(productId) ?: return
    val favs     by productVm.favorites.collectAsStateWithLifecycle()
    val cart     by productVm.cart.collectAsStateWithLifecycle()
    val isFav    = favs.any { it.id == product.id }
    val inCart   = cart.any { it.id == product.id }
    val catName  = categoryVm.getById(product.categoryId)?.name ?: "—"
    val catEmoji = categoryVm.getById(product.categoryId)?.emoji ?: ""

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("edit_product/${product.id}") }) {
                        Icon(Icons.Filled.Edit, "Editar")
                    }
                    IconButton(onClick = {
                        productVm.deleteProduct(product.id)
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Filled.Delete, "Eliminar",
                            tint = MaterialTheme.colorScheme.error)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Imagen grande
            Box {
                AsyncImage(
                    model              = product.imageUrl,
                    contentDescription = product.name,
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier.fillMaxWidth().height(280.dp)
                        .clip(MaterialTheme.shapes.medium)
                )
                // Badges sobre imagen
                Row(modifier = Modifier.padding(12.dp).align(Alignment.TopStart),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (product.isNew) Badge(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    ) { Text("NUEVO") }
                    if (product.isFeatured) Badge(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ) { Text("DESTACADO") }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                // Nombre y favorito
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.Top
                ) {
                    Text(product.name,
                        style    = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f))
                    IconButton(onClick = { productVm.toggleFavorite(product) }) {
                        Icon(
                            imageVector = if (isFav) Icons.Filled.Favorite
                            else Icons.Filled.FavoriteBorder,
                            tint = if (isFav) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.onSurface,
                            contentDescription = "Favorito"
                        )
                    }
                }

                // Categoría
                AssistChip(onClick = {}, label = { Text("$catEmoji $catName") })
                Spacer(Modifier.height(8.dp))

                // Rating
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    RatingBar(product.rating)
                    Text("${product.rating}/5 · ${product.reviewCount} reseñas",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(Modifier.height(12.dp))

                // Precio
                PriceTag(price = product.price, originalPrice = product.originalPrice)
                Spacer(Modifier.height(4.dp))

                // Stock
                if (product.stock <= 5) {
                    Text("⚠️ ¡Solo ${product.stock} unidades disponibles!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error)
                } else {
                    Text("✅ En stock (${product.stock} disponibles)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary)
                }

                Divider(modifier = Modifier.padding(vertical = 16.dp))

                // Descripción
                Text("Descripción", style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(product.description, style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)

                Spacer(Modifier.height(24.dp))

                // Botones de acción
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AppButton(
                        text     = if (inCart) "✓ En carrito" else "Agregar al carrito",
                        modifier = Modifier.weight(1f),
                        icon = {
                            Icon(
                                imageVector = if (inCart) Icons.Filled.ShoppingCart
                                else Icons.Outlined.ShoppingCart,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        onClick = { productVm.toggleCart(product) }
                    )
                }
                Spacer(Modifier.height(80.dp))
            }
        }
    }
}