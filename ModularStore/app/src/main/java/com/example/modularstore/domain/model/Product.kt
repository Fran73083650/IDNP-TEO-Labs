package com.example.modularstore.domain.model

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val originalPrice: Double = 0.0,   // para mostrar descuento
    val imageUrl: String = "",
    val categoryId: Int = 1,
    val rating: Float = 0f,
    val reviewCount: Int = 0,
    val stock: Int = 10,
    val isNew: Boolean = false,
    val isFeatured: Boolean = false
)