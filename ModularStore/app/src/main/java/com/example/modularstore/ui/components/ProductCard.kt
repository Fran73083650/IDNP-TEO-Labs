package com.example.modularstore.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.modularstore.domain.model.Product

@Composable
fun ProductCard(
    product: Product,
    categoryName: String,
    isFavorite: Boolean,
    isInCart: Boolean,
    onViewDetail: (Product) -> Unit,
    onToggleFavorite: (Product) -> Unit,
    onToggleCart: (Product) -> Unit,
    onEdit: (Product) -> Unit,
    onDelete: (Product) -> Unit
) {
    Card(
        modifier  = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        onClick   = { onViewDetail(product) }
    ) {
        Column {
            Box {
                if (product.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model              = product.imageUrl,
                        contentDescription = product.name,
                        contentScale       = ContentScale.Crop,
                        modifier           = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(MaterialTheme.shapes.medium)
                    )
                }
                // Badges
                Row(
                    modifier = Modifier.padding(8.dp).align(Alignment.TopStart),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (product.isNew) Badge(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    ) { Text("NUEVO", style = MaterialTheme.typography.labelMedium) }
                    if (product.originalPrice > product.price) {
                        val d = ((1 - product.price / product.originalPrice) * 100).toInt()
                        Badge(containerColor = MaterialTheme.colorScheme.error) {
                            Text("-$d%", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
                // Botón favorito sobre imagen
                IconButton(
                    onClick  = { onToggleFavorite(product) },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite
                        else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (isFavorite) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(product.name, style = MaterialTheme.typography.titleMedium,
                    maxLines = 1)
                Text(categoryName, style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RatingBar(rating = product.rating)
                    Spacer(Modifier.width(4.dp))
                    Text("(${product.reviewCount})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(Modifier.height(6.dp))
                PriceTag(price = product.price, originalPrice = product.originalPrice)
                if (product.stock <= 5) {
                    Text("¡Solo ${product.stock} en stock!",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.error)
                }
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    // Carrito — toggle con color
                    IconButton(onClick = { onToggleCart(product) }) {
                        Icon(
                            imageVector = if (isInCart) Icons.Filled.ShoppingCart
                            else Icons.Outlined.ShoppingCart,
                            contentDescription = "Carrito",
                            tint = if (isInCart) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(onClick = { onEdit(product) }) {
                            Icon(Icons.Filled.Edit, "Editar",
                                tint = MaterialTheme.colorScheme.secondary)
                        }
                        IconButton(onClick = { onDelete(product) }) {
                            Icon(Icons.Filled.Delete, "Eliminar",
                                tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}