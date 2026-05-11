package com.example.gestiontareas.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gestiontareas.data.calcularRacha
import com.example.gestiontareas.data.completadoHoy
import com.example.gestiontareas.viewmodel.HabitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDetailScreen(
    habitId: Int,
    viewModel: HabitViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val habit = state.habitos.find { it.id == habitId }
        ?: state.todosLosHabitos.find { it.id == habitId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(habit?.nombre ?: "Detalle") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor    = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        if (habit == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Hábito no encontrado")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Tarjeta de resumen
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors   = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier            = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (habit.descripcion.isNotBlank()) {
                        Text(habit.descripcion, style = MaterialTheme.typography.bodyMedium)
                    }
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text  = "🔥 ${habit.calcularRacha()}",
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Text("Racha actual", style = MaterialTheme.typography.labelSmall)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text  = "📅 ${habit.historial.size}",
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Text("Total días", style = MaterialTheme.typography.labelSmall)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text  = if (habit.completadoHoy()) "✅" else "⏳",
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Text("Hoy", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Text("Historial", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            if (habit.historial.isEmpty()) {
                Text(
                    text  = "Sin registros aún. ¡Marca el hábito hoy!",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(habit.historial.sortedDescending()) { fecha ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text     = "✅  $fecha",
                                modifier = Modifier.padding(12.dp),
                                style    = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}