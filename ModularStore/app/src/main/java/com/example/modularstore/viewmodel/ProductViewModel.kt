package com.example.modularstore.viewmodel

import androidx.lifecycle.ViewModel
import com.example.modularstore.data.repository.ProductRepository
import com.example.modularstore.data.repository.ProductRepositoryImpl
import com.example.modularstore.domain.model.Product
import com.example.modularstore.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProductViewModel(
    private val repository: ProductRepository = ProductRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Product>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Product>>> = _uiState.asStateFlow()

    private val _featured = MutableStateFlow<List<Product>>(emptyList())
    val featured: StateFlow<List<Product>> = _featured.asStateFlow()

    private val _newArrivals = MutableStateFlow<List<Product>>(emptyList())
    val newArrivals: StateFlow<List<Product>> = _newArrivals.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategoryId = MutableStateFlow<Int?>(null)
    val selectedCategoryId: StateFlow<Int?> = _selectedCategoryId.asStateFlow()

    private val _favorites = MutableStateFlow<List<Product>>(emptyList())
    val favorites: StateFlow<List<Product>> = _favorites.asStateFlow()

    private val _cart = MutableStateFlow<List<Product>>(emptyList())
    val cart: StateFlow<List<Product>> = _cart.asStateFlow()

    val cartTotal: Double get() = _cart.value.sumOf { it.price }
    val cartCount: Int    get() = _cart.value.size

    init { loadAll() }

    fun loadAll() {
        _featured.value    = repository.getFeatured()
        _newArrivals.value = repository.getNew()
        loadProducts()
    }

    fun loadProducts() {
        val query    = _searchQuery.value
        val catId    = _selectedCategoryId.value
        val base     = if (query.isNotBlank()) repository.search(query) else repository.getAll()
        val filtered = if (catId != null) base.filter { it.categoryId == catId } else base
        _uiState.value = if (filtered.isEmpty()) UiState.Empty else UiState.Success(filtered)
    }

    fun onSearchQuery(q: String) { _searchQuery.value = q; loadProducts() }
    fun onCategorySelected(id: Int?) { _selectedCategoryId.value = id; loadProducts() }

    fun addProduct(name: String, desc: String, price: String,
                   imageUrl: String, categoryId: Int, isNew: Boolean, isFeatured: Boolean) {
        val p = price.toDoubleOrNull()
        if (name.isBlank() || p == null || p <= 0) {
            _uiState.value = UiState.Error("Completa todos los campos correctamente")
            return
        }
        repository.insert(Product(0, name.trim(), desc.trim(), p,
            imageUrl = imageUrl.trim(), categoryId = categoryId,
            isNew = isNew, isFeatured = isFeatured))
        loadAll()
    }

    fun updateProduct(id: Int, name: String, desc: String, price: String,
                      imageUrl: String, categoryId: Int, isNew: Boolean, isFeatured: Boolean) {
        val p = price.toDoubleOrNull()
        if (name.isBlank() || p == null || p <= 0) {
            _uiState.value = UiState.Error("Completa todos los campos correctamente")
            return
        }
        repository.update(Product(id, name.trim(), desc.trim(), p,
            imageUrl = imageUrl.trim(), categoryId = categoryId,
            isNew = isNew, isFeatured = isFeatured))
        loadAll()
    }

    fun deleteProduct(id: Int) { repository.delete(id); loadAll() }
    fun getById(id: Int)       = repository.getById(id)

    fun toggleFavorite(product: Product) {
        _favorites.value =
            if (_favorites.value.any { it.id == product.id })
                _favorites.value.filter { it.id != product.id }
            else _favorites.value + product
    }

    fun toggleCart(product: Product) {
        _cart.value =
            if (_cart.value.any { it.id == product.id })
                _cart.value.filter { it.id != product.id }
            else _cart.value + product
    }

    fun isFavorite(product: Product) = _favorites.value.any { it.id == product.id }
    fun isInCart(product: Product)   = _cart.value.any { it.id == product.id }
    fun removeFromCart(id: Int)      { _cart.value = _cart.value.filter { it.id != id } }
}