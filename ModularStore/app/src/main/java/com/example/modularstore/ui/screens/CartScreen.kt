package com.example.modularstore.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.modularstore.ui.components.AppButton
import com.example.modularstore.ui.components.EmptyState
import com.example.modularstore.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(productVm: ProductViewModel) {
    val cart by productVm.cart.collectAsStateWithLifecycle()

    Scaffold(topBar = { TopAppBar(title = { Text("Mi carrito 🛒") }) }) { padding ->
        if (cart.isEmpty()) {
            EmptyState(Icons.Filled.ShoppingCart, "Carrito vacío",
                "Agrega productos desde la tienda")
        } else {
            Column(modifier = Modifier.padding(padding)) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(cart, key = { it.id }) { product ->
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)) {
                            Row(
                                modifier          = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model              = product.imageUrl,
                                    contentDescription = product.name,
                                    contentScale       = ContentScale.Crop,
                                    modifier           = Modifier.size(72.dp)
                                        .clip(MaterialTheme.shapes.medium)
                                )
                                Spacer(Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(product.name,
                                        style = MaterialTheme.typography.titleSmall)
                                    Text("$${String.format("%,.2f", product.price)}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary)
                                }
                                IconButton(onClick = { productVm.removeFromCart(product.id) }) {
                                    Icon(Icons.Filled.Delete, "Eliminar",
                                        tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }

                // Resumen del carrito
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    colors   = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Subtotal (${cart.size} items)")
                            Text("$${String.format("%,.2f", productVm.cartTotal)}")
                        }
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Envío"); Text("Gratis", color = MaterialTheme.colorScheme.primary)
                        }
                        Divider()
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total", style = MaterialTheme.typography.titleMedium)
                            Text("$${String.format("%,.2f", productVm.cartTotal)}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary)
                        }
                        Spacer(Modifier.height(4.dp))
                        AppButton(text = "Finalizar compra",
                            modifier = Modifier.fillMaxWidth(), onClick = {})
                    }
                }
            }
        }
    }
}