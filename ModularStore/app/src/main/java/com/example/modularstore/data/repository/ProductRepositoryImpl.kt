package com.example.modularstore.data.repository

import com.example.modularstore.domain.model.Product

class ProductRepositoryImpl : ProductRepository {

    private val items = mutableListOf(
        Product(1,  "Laptop Gamer RTX",    "Procesador Ryzen 9, 32GB RAM, RTX 4070. Ideal para gaming y creación de contenido profesional.", 2500.0, 2999.0, "https://www.lacuracao.pe/media/catalog/product/8/3/83nn0032lm_1.jpg?quality=85&bg-color=255,255,255&fit=bounds&height=&width=&canvas=:",    1, 4.8f, 128, 5,  isNew = false, isFeatured = true),
        Product(2,  "Teclado Mecánico",    "Switch Blue RGB, retroiluminación completa, construcción aluminio. Respuesta táctil perfecta.",   120.0,  150.0,  "https://img.kwcdn.com/product/fancy/a4ed63d3-860b-4185-938e-abd919754a08.jpg?imageView2/2/w/800/q/70/format/avif",  2, 4.6f, 89,  12, isNew = false, isFeatured = false),
        Product(3,  "Mouse Gaming 16K",    "16000 DPI, 7 botones programables, sensor óptico de alta precisión. Sin lag garantizado.",        75.0,   99.0,   "https://coolboxpe.vtexassets.com/arquivos/ids/186995-1200-1200?v=637607711278930000&width=1200&height=1200&aspect=true",     2, 4.5f, 204, 20, isNew = false, isFeatured = false),
        Product(4,  "Monitor 4K 144Hz",    "Panel IPS 27\", 144Hz, 1ms, HDR400. Colores perfectos para trabajo y gaming competitivo.",        650.0,  799.0,  "https://simple.ripley.com.pe/product/_next/image?url=https%3A%2F%2Frimage.ripley.com.pe%2Fhome.ripley%2FAttachment%2FMKP%2F3278%2FPMP20000439806%2Ffull_image-1.jpeg&w=640&q=100",   3, 4.9f, 56,  3,  isNew = false, isFeatured = true),
        Product(5,  "Headset 7.1 Surround","Audio envolvente virtual, micrófono con cancelación de ruido, almohadillas memory foam.",         90.0,   120.0,  "https://media.falabella.com/falabellaPE/146874950_01/w=1200,h=1200,fit=pad",   4, 4.4f, 312, 15, isNew = true,  isFeatured = false),
        Product(6,  "SSD NVMe 2TB",        "7000MB/s lectura, PCIe 4.0, disipador incluido. Carga instantánea en cualquier juego.",           180.0,  220.0,  "https://media.falabella.com/falabellaPE/146298355_01/w=1200,h=1200,fit=pad",       5, 4.7f, 178, 8,  isNew = false, isFeatured = false),
        Product(7,  "Silla Gamer Pro",     "Soporte lumbar ajustable, apoyabrazos 4D, reclinación 180°. Comodidad para largas sesiones.",      350.0,  450.0,  "https://media.falabella.com/sodimacPE/9142053_01/w=1200,h=1200,fit=pad",     6, 4.3f, 67,  4,  isNew = true,  isFeatured = true),
        Product(8,  "Webcam 4K 60fps",     "Autofoco rápido, corrección de luz HDR, micrófono estéreo integrado. Perfecta para streams.",     149.0,  189.0,  "https://picsum.photos/seed/webcam/600/400",    7, 4.6f, 93,  11, isNew = true,  isFeatured = false),
        Product(9,  "RAM DDR5 32GB",       "6000MHz, latencia CL36, kit doble canal. Compatible con plataformas Intel y AMD.",                 189.0,  230.0,  "https://picsum.photos/seed/ram/600/400",       5, 4.5f, 44,  9,  isNew = true,  isFeatured = false),
        Product(10, "Pad Mouse XL",        "90x40cm, superficie optimizada para sensor, base antideslizante. Cubre todo el escritorio.",       35.0,   49.0,   "https://picsum.photos/seed/pad/600/400",       2, 4.2f, 521, 30, isNew = false, isFeatured = false)
    )
    private var nextId = 11

    override fun getAll()              = items.toList()
    override fun getById(id: Int)      = items.find { it.id == id }
    override fun getFeatured()         = items.filter { it.isFeatured }
    override fun getNew()              = items.filter { it.isNew }
    override fun getByCategory(cId: Int) = items.filter { it.categoryId == cId }

    override fun search(query: String) = items.filter {
        it.name.contains(query, true) || it.description.contains(query, true)
    }

    override fun insert(p: Product)    { items.add(p.copy(id = nextId++)) }
    override fun update(p: Product)    { val i = items.indexOfFirst { it.id == p.id }; if (i >= 0) items[i] = p }
    override fun delete(id: Int)       { items.removeAll { it.id == id } }
}