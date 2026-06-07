package com.example.modularstore.viewmodel

import androidx.lifecycle.ViewModel
import com.example.modularstore.data.repository.CategoryRepository
import com.example.modularstore.data.repository.CategoryRepositoryImpl
import com.example.modularstore.domain.model.Category
import com.example.modularstore.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CategoryViewModel(
    private val repository: CategoryRepository = CategoryRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Category>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Category>>> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        val list = repository.getAll()
        _uiState.value = if (list.isEmpty()) UiState.Empty else UiState.Success(list)
    }

    // ✅ getAll() — usado en AddEditProductScreen para el dropdown
    fun getAll(): List<Category> = repository.getAll()

    fun getById(id: Int): Category? = repository.getById(id)

    // ✅ add() — usado en CategoryScreen
    fun add(name: String, desc: String, emoji: String) {
        if (name.isBlank()) return
        repository.insert(Category(0, name.trim(), desc.trim(), emoji.ifBlank { "📦" }))
        load()
    }

    // ✅ update() — usado en CategoryScreen
    fun update(id: Int, name: String, desc: String, emoji: String) {
        if (name.isBlank()) return
        repository.update(Category(id, name.trim(), desc.trim(), emoji.ifBlank { "📦" }))
        load()
    }

    // ✅ delete() — usado en CategoryScreen
    fun delete(id: Int) {
        repository.delete(id)
        load()
    }
}