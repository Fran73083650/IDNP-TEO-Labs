package com.example.gestiontareas.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.gestiontareas.data.Prioridad
import com.example.gestiontareas.data.Task
import com.example.gestiontareas.ui.theme.ColorAlta
import com.example.gestiontareas.ui.theme.ColorBaja
import com.example.gestiontareas.ui.theme.ColorMedia

@Composable
fun TaskCard(
    task: Task,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val colorPrioridad = when (task.prioridad) {
        Prioridad.ALTA  -> ColorAlta
        Prioridad.MEDIA -> ColorMedia
        Prioridad.BAJA  -> ColorBaja
    }

    val cardColor by animateColorAsState(
        targetValue = if (task.completada)
            MaterialTheme.colorScheme.surfaceVariant
        else
            MaterialTheme.colorScheme.surface,
        label = "cardColor"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (task.completada) 0.dp else 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indicador de prioridad
            Surface(
                modifier = Modifier
                    .width(4.dp)
                    .height(48.dp),
                color  = colorPrioridad,
                shape  = MaterialTheme.shapes.small
            ) {}

            Spacer(Modifier.width(8.dp))

            Checkbox(
                checked         = task.completada,
                onCheckedChange = { onToggle() }
            )

            Column(modifier = Modifier.weight(1f).padding(start = 4.dp)) {
                Text(
                    text           = task.titulo,
                    style          = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (task.completada) TextDecoration.LineThrough else TextDecoration.None,
                    maxLines       = 1,
                    overflow       = TextOverflow.Ellipsis
                )
                if (task.descripcion.isNotBlank()) {
                    Text(
                        text      = task.descripcion,
                        style     = MaterialTheme.typography.bodySmall,
                        color     = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines  = 1,
                        overflow  = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text  = task.prioridad.name,
                    style = MaterialTheme.typography.labelSmall,
                    color = colorPrioridad
                )
            }

            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Editar",
                    tint = MaterialTheme.colorScheme.primary)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}