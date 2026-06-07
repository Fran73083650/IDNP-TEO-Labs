package com.example.modularstore.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.modularstore.domain.model.Category
import com.example.modularstore.ui.components.AppButton
import com.example.modularstore.ui.components.EmptyState
import com.example.modularstore.ui.state.UiState
import com.example.modularstore.viewmodel.CategoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    navController: NavController,
    categoryVm: CategoryViewModel
) {
    val uiState      by categoryVm.uiState.collectAsStateWithLifecycle()
    var showDialog   by remember { mutableStateOf(false) }
    var editCategory by remember { mutableStateOf<Category?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Categorías") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { editCategory = null; showDialog = true }) {
                Icon(Icons.Filled.Add, "Nueva categoría")
            }
        }
    ) { padding ->
        when (val state = uiState) {
            is UiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Empty -> {
                EmptyState(
                    icon     = Icons.Filled.List,
                    title    = "Sin categorías",
                    subtitle = "Toca + para agregar la primera"
                )
            }
            is UiState.Error -> {
                EmptyState(
                    icon     = Icons.Filled.Warning,
                    title    = "Error",
                    subtitle = state.message
                )
            }
            is UiState.Success -> {
                LazyColumn(
                    modifier            = Modifier.padding(padding),
                    contentPadding      = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.data, key = { category -> category.id }) { category ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier              = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment     = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment     = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier              = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text  = category.emoji,
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                    Column {
                                        Text(
                                            text  = category.name,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        if (category.description.isNotBlank()) {
                                            Text(
                                                text  = category.description,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                                Row {
                                    IconButton(onClick = {
                                        editCategory = category
                                        showDialog = true
                                    }) {
                                        Icon(Icons.Filled.Edit, contentDescription = "Editar")
                                    }
                                    IconButton(onClick = {
                                        // ✅ llamada explícita para evitar conflicto con WindowInsets.add
                                        categoryVm.delete(category.id)
                                    }) {
                                        Icon(
                                            imageVector        = Icons.Filled.Delete,
                                            contentDescription = "Eliminar",
                                            tint               = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        CategoryDialog(
            category  = editCategory,
            onConfirm = { name, desc, emoji ->
                if (editCategory != null) {
                    // ✅ update explícito
                    categoryVm.update(editCategory!!.id, name, desc, emoji)
                } else {
                    // ✅ add explícito — sin wildcard imports que confunden al compilador
                    categoryVm.add(name, desc, emoji)
                }
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
private fun CategoryDialog(
    category: Category?,
    onConfirm: (String, String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var name  by remember { mutableStateOf(category?.name  ?: "") }
    var desc  by remember { mutableStateOf(category?.description ?: "") }
    var emoji by remember { mutableStateOf(category?.emoji ?: "📦") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (category != null) "Editar categoría" else "Nueva categoría")
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value         = emoji,
                    onValueChange = { emoji = it },
                    label         = { Text("Emoji") },
                    singleLine    = true,
                    modifier      = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value         = name,
                    onValueChange = { name = it },
                    label         = { Text("Nombre *") },
                    singleLine    = true,
                    modifier      = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value         = desc,
                    onValueChange = { desc = it },
                    label         = { Text("Descripción") },
                    modifier      = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            AppButton(text = "Guardar") { onConfirm(name, desc, emoji) }
        },
        dismissButton = {
            AppButton(text = "Cancelar", outlined = true) { onDismiss() }
        }
    )
}