package com.example.modularstore.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.modularstore.domain.model.Product
import com.example.modularstore.ui.components.*
import com.example.modularstore.ui.state.UiState
import com.example.modularstore.viewmodel.CategoryViewModel
import com.example.modularstore.viewmodel.ProductViewModel
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.outlined.ShoppingCart
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    productVm: ProductViewModel,
    categoryVm: CategoryViewModel
) {
    val featured     by productVm.featured.collectAsStateWithLifecycle()
    val newArrivals  by productVm.newArrivals.collectAsStateWithLifecycle()
    val productState by productVm.uiState.collectAsStateWithLifecycle()
    val catState     by categoryVm.uiState.collectAsStateWithLifecycle()
    val query        by productVm.searchQuery.collectAsStateWithLifecycle()
    val selectedCat  by productVm.selectedCategoryId.collectAsStateWithLifecycle()
    val cartItems    by productVm.cart.collectAsStateWithLifecycle()
    val favItems     by productVm.favorites.collectAsStateWithLifecycle()

    val categories = when (val s = catState) { is UiState.Success -> s.data; else -> emptyList() }
    val catMap     = categories.associateBy { it.id }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Modular Store", style = MaterialTheme.typography.titleLarge)
                        Text("¡Bienvenido!", style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("categories") }) {
                        Icon(Icons.Filled.List, "Categorías")
                    }
                    BadgedBox(badge = {
                        if (cartItems.isNotEmpty())
                            Badge { Text(cartItems.size.toString()) }
                    }) {
                        IconButton(onClick = { navController.navigate("cart") }) {
                            Icon(Icons.Filled.ShoppingCart, "Carrito")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick           = { navController.navigate("add_product") },
                containerColor    = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, "Agregar", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {

            // Búsqueda
            item {
                SearchBar(query = query, onQueryChange = { productVm.onSearchQuery(it) })
            }

            // Banner principal
            if (query.isBlank() && selectedCat == null) {
                item {
                    BannerSection()
                    Spacer(Modifier.height(8.dp))
                }

                // Categorías con emojis
                item {
                    SectionTitle("Categorías")
                    LazyRow(
                        contentPadding        = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(categories) { cat ->
                            CategoryChip(
                                emoji      = cat.emoji,
                                name       = cat.name,
                                isSelected = selectedCat == cat.id,
                                onClick    = {
                                    productVm.onCategorySelected(
                                        if (selectedCat == cat.id) null else cat.id
                                    )
                                }
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }

                // Destacados
                if (featured.isNotEmpty()) {
                    item {
                        SectionTitle("⭐ Destacados")
                        LazyRow(
                            contentPadding        = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(featured, key = { it.id }) { product ->
                                MiniProductCard(
                                    product      = product,
                                    categoryName = catMap[product.categoryId]?.name ?: "",
                                    isFavorite   = favItems.any { it.id == product.id },
                                    isInCart     = cartItems.any { it.id == product.id },
                                    onClick      = { navController.navigate("detail/${product.id}") },
                                    onFavorite   = { productVm.toggleFavorite(product) },
                                    onCart       = { productVm.toggleCart(product) }
                                )
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                    }
                }

                // Nuevos
                if (newArrivals.isNotEmpty()) {
                    item {
                        SectionTitle("🆕 Nuevos llegados")
                        LazyRow(
                            contentPadding        = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(newArrivals, key = { it.id }) { product ->
                                MiniProductCard(
                                    product      = product,
                                    categoryName = catMap[product.categoryId]?.name ?: "",
                                    isFavorite   = favItems.any { it.id == product.id },
                                    isInCart     = cartItems.any { it.id == product.id },
                                    onClick      = { navController.navigate("detail/${product.id}") },
                                    onFavorite   = { productVm.toggleFavorite(product) },
                                    onCart       = { productVm.toggleCart(product) }
                                )
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                    }
                }

                item { SectionTitle("Todos los productos") }
            }

            // Lista completa / resultados de búsqueda
            when (val state = productState) {
                is UiState.Loading -> item { Box(Modifier.fillMaxWidth().height(200.dp),
                    Alignment.Center) { CircularProgressIndicator() } }
                is UiState.Empty   -> item { EmptyState(
                    icon     = Icons.Filled.SearchOff,
                    title    = "Sin resultados",
                    subtitle = "Prueba con otro término o categoría"
                ) }
                is UiState.Error   -> item { EmptyState(
                    icon = Icons.Filled.Warning, title = "Error", subtitle = state.message) }
                is UiState.Success -> items(state.data, key = { it.id }) { product ->
                    ProductCard(
                        product          = product,
                        categoryName     = catMap[product.categoryId]?.name ?: "—",
                        isFavorite       = favItems.any { it.id == product.id },
                        isInCart         = cartItems.any { it.id == product.id },
                        onViewDetail     = { navController.navigate("detail/${it.id}") },
                        onToggleFavorite = { productVm.toggleFavorite(it) },
                        onToggleCart     = { productVm.toggleCart(it) },
                        onEdit           = { navController.navigate("edit_product/${it.id}") },
                        onDelete         = { productVm.deleteProduct(it.id) }
                    )
                }
            }
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

// ── Sub-composables SRP ───────────────────────────────────────────────────────

@Composable
fun SectionTitle(title: String) {
    Text(
        text     = title,
        style    = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun BannerSection() {
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp).height(160.dp),
        colors   = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier          = Modifier.fillMaxSize().padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("¡Ofertas del día!", style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold)
                Text("Hasta 30% de descuento", style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(12.dp))
                Text("Ver ofertas →", style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            Icon(Icons.Filled.LocalOffer, null,
                modifier = Modifier.size(72.dp),
                tint     = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
        }
    }
}

@Composable
fun CategoryChip(
    emoji: String,
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick  = onClick,
        label    = { Text("$emoji $name") }
    )
}

@Composable
fun MiniProductCard(
    product: Product,
    categoryName: String,
    isFavorite: Boolean,
    isInCart: Boolean,
    onClick: () -> Unit,
    onFavorite: () -> Unit,
    onCart: () -> Unit
) {
    Card(
        modifier  = Modifier.width(180.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick   = onClick
    ) {
        Column {
            Box {
                AsyncImage(
                    model              = product.imageUrl,
                    contentDescription = product.name,
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier.height(120.dp).fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                )
                IconButton(onClick = onFavorite,
                    modifier = Modifier.align(Alignment.TopEnd).size(36.dp)) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite
                        else Icons.Filled.FavoriteBorder,
                        contentDescription = null,
                        tint = if (isFavorite) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Column(modifier = Modifier.padding(8.dp)) {
                Text(product.name, style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold, maxLines = 1)
                Text(categoryName, style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary)
                RatingBar(product.rating)
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text("$${String.format("%,.0f", product.price)}",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold)
                    IconButton(onClick = onCart, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = if (isInCart) Icons.Filled.ShoppingCart
                            else Icons.Outlined.ShoppingCart,
                            contentDescription = null,
                            tint = if (isInCart) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}