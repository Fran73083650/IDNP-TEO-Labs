package com.example.gestiontareas.data

import java.text.SimpleDateFormat
import java.util.*

data class Habit(
    val id: Int = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
    val nombre: String,
    val descripcion: String = "",
    val color: String = "AZUL",
    val fechaCreacion: Long = System.currentTimeMillis(),
    val historial: List<String> = emptyList()
)

enum class HabitColor(val hex: String) {
    AZUL("#2196F3"),
    VERDE("#4CAF50"),
    NARANJA("#FF9800"),
    MORADO("#9C27B0"),
    ROJO("#F44336")
}

private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

fun fechaHoy(): String = sdf.format(Date())

fun fechaAnteriores(diasAtras: Int): String {
    val cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, -diasAtras)
    return sdf.format(cal.time)
}

fun Habit.completadoHoy(): Boolean = historial.contains(fechaHoy())

fun Habit.calcularRacha(): Int {
    if (historial.isEmpty()) return 0
    val sorted = historial.sortedDescending()
    var racha = 0
    var diasAtras = 0
    for (fecha in sorted) {
        val esperado = fechaAnteriores(diasAtras)
        if (fecha == esperado) {
            racha++
            diasAtras++
        } else break
    }
    return racha
}