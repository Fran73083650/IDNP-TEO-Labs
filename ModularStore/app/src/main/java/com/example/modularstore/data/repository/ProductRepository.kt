package com.example.modularstore.data.repository

import com.example.modularstore.domain.model.Product

interface ProductRepository {
    fun getAll(): List<Product>
    fun getById(id: Int): Product?
    fun getFeatured(): List<Product>
    fun getNew(): List<Product>
    fun search(query: String): List<Product>
    fun getByCategory(categoryId: Int): List<Product>
    fun insert(product: Product)
    fun update(product: Product)
    fun delete(id: Int)
}