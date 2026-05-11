package com.example.gestiontareas.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gestiontareas.data.Prioridad
import com.example.gestiontareas.data.Task

@Composable
fun TaskDialog(
    tareaExistente: Task?,
    onConfirmar: (titulo: String, descripcion: String, prioridad: Prioridad) -> Unit,
    onCancelar: () -> Unit
) {
    var titulo      by remember { mutableStateOf(tareaExistente?.titulo ?: "") }
    var descripcion by remember { mutableStateOf(tareaExistente?.descripcion ?: "") }
    var prioridad   by remember { mutableStateOf(tareaExistente?.prioridad ?: Prioridad.MEDIA) }

    AlertDialog(
        onDismissRequest = onCancelar,
        title   = { Text(if (tareaExistente == null) "Nueva Tarea" else "Editar Tarea") },
        text    = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value         = titulo,
                    onValueChange = { titulo = it },
                    label         = { Text("Título *") },
                    modifier      = Modifier.fillMaxWidth(),
                    singleLine    = true
                )
                OutlinedTextField(
                    value         = descripcion,
                    onValueChange = { descripcion = it },
                    label         = { Text("Descripción (opcional)") },
                    modifier      = Modifier.fillMaxWidth(),
                    maxLines      = 3
                )
                Text("Prioridad:", style = MaterialTheme.typography.labelLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Prioridad.entries.forEach { p ->
                        FilterChip(
                            selected = prioridad == p,
                            onClick  = { prioridad = p },
                            label    = { Text(p.name) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (titulo.isNotBlank()) onConfirmar(titulo, descripcion, prioridad) },
                enabled = titulo.isNotBlank()
            ) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) { Text("Cancelar") }
        }
    )
}