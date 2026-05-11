package com.example.gestiontareas.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onGoToTasks: () -> Unit,
    onGoToHabits: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title  = { Text("Mi App") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor    = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = onToggleTheme) {
                        Icon(
                            imageVector        = if (darkTheme) Icons.Default.LightMode
                            else Icons.Default.DarkMode,
                            contentDescription = "Cambiar tema"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier              = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement   = Arrangement.Center,
            horizontalAlignment   = Alignment.CenterHorizontally
        ) {
            Text(
                text  = "¿Qué deseas gestionar?",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(32.dp))

            Button(
                onClick  = onGoToTasks,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("📋  Gestión de Tareas", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick  = onGoToHabits,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("🔥  Gestión de Hábitos", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}