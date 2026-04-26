package com.example.gestiontareas.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "tasks")

class TaskRepository(private val context: Context) {  // ← Context, NO TaskDao

    companion object {
        private val TASKS_KEY = stringPreferencesKey("tasks_list")
    }

    private fun Task.toJson(): String {
        return "${id}|${titulo}|${descripcion}|${completada}|${prioridad.name}|${fechaCreacion}"
    }

    private fun String.toTask(): Task? {
        return try {
            val parts = this.split("|")
            Task(
                id            = parts[0].toInt(),
                titulo        = parts[1],
                descripcion   = parts[2],
                completada    = parts[3].toBoolean(),
                prioridad     = Prioridad.valueOf(parts[4]),
                fechaCreacion = parts[5].toLong()
            )
        } catch (e: Exception) { null }
    }

    private fun List<Task>.serialize(): String =
        joinToString(";;") { it.toJson() }

    private fun String.deserialize(): List<Task> =
        if (isBlank()) emptyList()
        else split(";;").mapNotNull { it.toTask() }

    val tasks: Flow<List<Task>> = context.dataStore.data.map { prefs ->
        prefs[TASKS_KEY]?.deserialize() ?: emptyList()
    }

    suspend fun saveTasks(tasks: List<Task>) {
        context.dataStore.edit { prefs ->
            prefs[TASKS_KEY] = tasks.serialize()
        }
    }
}