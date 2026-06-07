package com.example.modularstore.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.modularstore.ui.components.AppButton
import com.example.modularstore.ui.state.UiState
import com.example.modularstore.viewmodel.CategoryViewModel
import com.example.modularstore.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductScreen(
    navController: NavController,
    productVm: ProductViewModel,
    categoryVm: CategoryViewModel,
    productId: Int? = null
) {
    val isEdit   = productId != null
    val existing = if (isEdit) productVm.getById(productId!!) else null

    var name        by remember { mutableStateOf(existing?.name        ?: "") }
    var description by remember { mutableStateOf(existing?.description ?: "") }
    var price       by remember { mutableStateOf(existing?.price?.toString() ?: "") }
    var imageUrl    by remember { mutableStateOf(existing?.imageUrl    ?: "") }
    var categoryId  by remember { mutableStateOf(existing?.categoryId  ?: 1) }
    var isNew       by remember { mutableStateOf(existing?.isNew       ?: false) }
    var isFeatured  by remember { mutableStateOf(existing?.isFeatured  ?: false) }
    var expanded    by remember { mutableStateOf(false) }

    // ✅ getAll() ahora existe en CategoryViewModel
    val categories   = categoryVm.getAll()
    val productState by productVm.uiState.collectAsStateWithLifecycle()
    val errorMsg     = (productState as? UiState.Error)?.message

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEdit) "Editar producto" else "Nuevo producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Mensaje de error
            errorMsg?.let { msg ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text     = msg,
                        modifier = Modifier.padding(12.dp),
                        color    = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            OutlinedTextField(
                value         = name,
                onValueChange = { name = it },
                label         = { Text("Nombre *") },
                modifier      = Modifier.fillMaxWidth(),
                singleLine    = true
            )

            OutlinedTextField(
                value         = description,
                onValueChange = { description = it },
                label         = { Text("Descripción *") },
                modifier      = Modifier.fillMaxWidth(),
                minLines      = 3
            )

            OutlinedTextField(
                value           = price,
                onValueChange   = { price = it },
                label           = { Text("Precio *") },
                modifier        = Modifier.fillMaxWidth(),
                singleLine      = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            OutlinedTextField(
                value         = imageUrl,
                onValueChange = { imageUrl = it },
                label         = { Text("URL de imagen") },
                modifier      = Modifier.fillMaxWidth(),
                singleLine    = true
            )

            // Dropdown categoría — ✅ sin wildcard imports que confundan a Kotlin
            ExposedDropdownMenuBox(
                expanded         = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value    = categories.find { cat -> cat.id == categoryId }?.name
                        ?: "Seleccionar",
                    onValueChange = {},
                    readOnly      = true,
                    label         = { Text("Categoría *") },
                    trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier      = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded         = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { cat ->
                        DropdownMenuItem(
                            text    = { Text("${cat.emoji} ${cat.name}") },
                            onClick = {
                                categoryId = cat.id
                                expanded   = false
                            }
                        )
                    }
                }
            }

            // Switch — Producto nuevo
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Producto nuevo", style = MaterialTheme.typography.bodyMedium)
                Switch(checked = isNew, onCheckedChange = { isNew = it })
            }

            // Switch — Destacado
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Destacado", style = MaterialTheme.typography.bodyMedium)
                Switch(checked = isFeatured, onCheckedChange = { isFeatured = it })
            }

            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AppButton(
                    text     = "Cancelar",
                    outlined = true,
                    modifier = Modifier.weight(1f),
                    onClick  = { navController.popBackStack() }
                )
                AppButton(
                    text     = if (isEdit) "Guardar" else "Agregar",
                    modifier = Modifier.weight(1f),
                    onClick  = {
                        if (isEdit) {
                            productVm.updateProduct(
                                productId!!, name, description,
                                price, imageUrl, categoryId, isNew, isFeatured
                            )
                        } else {
                            productVm.addProduct(
                                name, description,
                                price, imageUrl, categoryId, isNew, isFeatured
                            )
                        }
                        if (productState !is UiState.Error) {
                            navController.popBackStack()
                        }
                    }
                )
            }
        }
    }
}