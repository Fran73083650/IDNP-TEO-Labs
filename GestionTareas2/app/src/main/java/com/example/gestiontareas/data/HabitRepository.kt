package com.example.gestiontareas.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.habitDataStore by preferencesDataStore(name = "habits")

class HabitRepository(private val context: Context) {

    companion object {
        private val HABITS_KEY = stringPreferencesKey("habits_list")
    }

    private fun Habit.toJson(): String {
        val historialStr = historial.joinToString(",")
        return "$id§$nombre§$descripcion§$color§$fechaCreacion§$historialStr"
    }

    private fun String.toHabit(): Habit? {
        return try {
            val parts = this.split("§")
            val historial = if (parts[5].isBlank()) emptyList()
            else parts[5].split(",")
            Habit(
                id           = parts[0].toInt(),
                nombre       = parts[1],
                descripcion  = parts[2],
                color        = parts[3],
                fechaCreacion = parts[4].toLong(),
                historial    = historial
            )
        } catch (e: Exception) { null }
    }

    private fun List<Habit>.serialize(): String =
        joinToString(";;") { it.toJson() }

    private fun String.deserialize(): List<Habit> =
        if (isBlank()) emptyList()
        else split(";;").mapNotNull { it.toHabit() }

    val habits: Flow<List<Habit>> = context.habitDataStore.data.map { prefs ->
        prefs[HABITS_KEY]?.deserialize() ?: emptyList()
    }

    suspend fun saveHabits(habits: List<Habit>) {
        context.habitDataStore.edit { prefs ->
            prefs[HABITS_KEY] = habits.serialize()
        }
    }
}