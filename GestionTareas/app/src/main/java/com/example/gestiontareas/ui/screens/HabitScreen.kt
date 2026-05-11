package com.example.gestiontareas.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gestiontareas.data.HabitColor
import com.example.gestiontareas.ui.components.FilterChipHabits
import com.example.gestiontareas.ui.components.HabitCard
import com.example.gestiontareas.viewmodel.FiltroHabit
import com.example.gestiontareas.viewmodel.HabitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitScreen(
    viewModel: HabitViewModel,
    onBack: () -> Unit,
    onHabitClick: (Int) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var mostrarDialogNuevo by remember { mutableStateOf(false) }
    var nuevoNombre by remember { mutableStateOf("") }
    var nuevaDesc by remember { mutableStateOf("") }
    var nuevoColor by remember { mutableStateOf(HabitColor.AZUL) }

    if (mostrarDialogNuevo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogNuevo = false },
            title = { Text("Nuevo Hábito") },
            text  = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value         = nuevoNombre,
                        onValueChange = { nuevoNombre = it },
                        label         = { Text("Nombre *") },
                        modifier      = Modifier.fillMaxWidth(),
                        singleLine    = true
                    )
                    OutlinedTextField(
                        value         = nuevaDesc,
                        onValueChange = { nuevaDesc = it },
                        label         = { Text("Descripción (opcional)") },
                        modifier      = Modifier.fillMaxWidth()
                    )
                    Text("Color:", style = MaterialTheme.typography.labelLarge)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        HabitColor.entries.forEach { c ->
                            FilterChip(
                                selected = nuevoColor == c,
                                onClick  = { nuevoColor = c },
                                label    = { Text(c.name) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick  = {
                        if (nuevoNombre.isNotBlank()) {
                            viewModel.agregarHabit(nuevoNombre, nuevaDesc, nuevoColor.name)
                            nuevoNombre = ""
                            nuevaDesc   = ""
                            nuevoColor  = HabitColor.AZUL
                            mostrarDialogNuevo = false
                        }
                    },
                    enabled = nuevoNombre.isNotBlank()
                ) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogNuevo = false }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hábitos") },
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
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { mostrarDialogNuevo = true }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar hábito")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            FilterChipHabits(
                filtroActual   = state.filtro,
                onFiltroChange = { viewModel.setFiltro(it) }
            )

            Spacer(Modifier.height(8.dp))

            if (state.habitos.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text  = "No hay hábitos. ¡Agrega uno! 💪",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(state.habitos, key = { it.id }) { habit ->
                        HabitCard(
                            habit       = habit,
                            onToggle    = { viewModel.toggleHoy(habit) },
                            onDelete    = { viewModel.eliminarHabit(habit) },
                            onClick     = { onHabitClick(habit.id) }
                        )
                    }
                }
            }
        }
    }
}