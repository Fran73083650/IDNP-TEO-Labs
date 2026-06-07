package com.example.modularstore.data.repository

import com.example.modularstore.domain.model.Category

class CategoryRepositoryImpl : CategoryRepository {
    private val items = mutableListOf(
        Category(1, "PC & Laptops",  "Computadoras de alto rendimiento", "💻"),
        Category(2, "Periféricos",   "Teclados, mouse y accesorios",     "🖱️"),
        Category(3, "Monitores",     "Pantallas 4K y gaming",            "🖥️"),
        Category(4, "Audio",         "Auriculares y sonido",             "🎧"),
        Category(5, "Almacenamiento","SSD, HDD y memorias",              "💾"),
        Category(6, "Mobiliario",    "Sillas y escritorios gamer",       "🪑"),
        Category(7, "Streaming",     "Cámaras y accesorios",             "📷")
    )
    private var nextId = 8

    override fun getAll()         = items.toList()
    override fun getById(id: Int) = items.find { it.id == id }
    override fun insert(c: Category) { items.add(c.copy(id = nextId++)) }
    override fun update(c: Category) { val i = items.indexOfFirst { it.id == c.id }; if (i >= 0) items[i] = c }
    override fun delete(id: Int)  { items.removeAll { it.id == id } }
}