package com.example.modularstore.data.repository

import com.example.modularstore.domain.model.Category

interface CategoryRepository {
    fun getAll(): List<Category>
    fun getById(id: Int): Category?
    fun insert(category: Category)
    fun update(category: Category)
    fun delete(id: Int)
}