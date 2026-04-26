package com.example.gestiontareas.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gestiontareas.ui.components.FiltroChips
import com.example.gestiontareas.ui.components.TaskCard
import com.example.gestiontareas.ui.components.TaskDialog
import com.example.gestiontareas.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(viewModel: TaskViewModel) {
    val state by viewModel.uiState.collectAsState()

    if (state.mostrarDialog) {
        TaskDialog(
            tareaExistente = state.tareaEditando,
            onConfirmar    = { titulo, desc, prio ->
                val editando = state.tareaEditando
                if (editando == null)
                    viewModel.agregarTarea(titulo, desc, prio)
                else
                    viewModel.editarTarea(editando, titulo, desc, prio)
                viewModel.cerrarDialog()
            },
            onCancelar = { viewModel.cerrarDialog() }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title  = { Text("Gestor de Tareas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor    = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = { viewModel.toggleTheme() }) {
                        Icon(
                            imageVector        = if (state.darkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Cambiar tema"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.abrirDialog() }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar tarea")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            FiltroChips(
                filtroActual    = state.filtro,
                onFiltroChange  = { viewModel.setFiltro(it) }
            )

            Spacer(Modifier.height(8.dp))

            if (state.tareas.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text  = "No hay tareas aquí 🎉",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(state.tareas, key = { it.id }) { task ->
                        TaskCard(
                            task     = task,
                            onToggle = { viewModel.toggleCompletada(task) },
                            onEdit   = { viewModel.abrirDialog(task) },
                            onDelete = { viewModel.eliminarTarea(task) }
                        )
                    }
                }
            }
        }
    }
}