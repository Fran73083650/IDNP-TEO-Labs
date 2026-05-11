package com.example.gestiontareas.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.gestiontareas.data.Habit
import com.example.gestiontareas.data.HabitColor
import com.example.gestiontareas.data.calcularRacha
import com.example.gestiontareas.data.completadoHoy

@Composable
fun HabitCard(
    habit: Habit,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    val completado = habit.completadoHoy()
    val racha      = habit.calcularRacha()

    val colorHex = HabitColor.entries
        .find { it.name == habit.color }?.hex ?: "#2196F3"
    val habitColor = Color(android.graphics.Color.parseColor(colorHex))

    val cardColor by animateColorAsState(
        targetValue = if (completado) habitColor.copy(alpha = 0.2f)
        else MaterialTheme.colorScheme.surface,
        label       = "habitCardColor"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors    = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (completado) 0.dp else 3.dp)
    ) {
        Row(
            modifier             = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment    = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.width(4.dp).height(48.dp),
                color    = habitColor,
                shape    = MaterialTheme.shapes.small
            ) {}

            Spacer(Modifier.width(8.dp))

            Checkbox(
                checked         = completado,
                onCheckedChange = { onToggle() }
            )

            Column(modifier = Modifier.weight(1f).padding(start = 4.dp)) {
                Text(
                    text  = habit.nombre,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text  = if (racha > 0) "🔥 Racha: $racha días" else "Sin racha aún",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (racha > 0) habitColor else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}