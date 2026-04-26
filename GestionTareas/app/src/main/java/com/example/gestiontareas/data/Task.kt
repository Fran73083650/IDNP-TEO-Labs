package com.example.gestiontareas.data

enum class Prioridad { BAJA, MEDIA, ALTA }

data class Task(
    val id: Int = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(), // ← Int, no Long
    val titulo: String,
    val descripcion: String = "",
    val completada: Boolean = false,
    val prioridad: Prioridad = Prioridad.MEDIA,
    val fechaCreacion: Long = System.currentTimeMillis()
)