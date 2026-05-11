package com.example.gestiontareas.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gestiontareas.viewmodel.FiltroHabit
import com.example.gestiontareas.viewmodel.FiltroTarea

@Composable
fun FiltroChips(
    filtroActual: FiltroTarea,
    onFiltroChange: (FiltroTarea) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FiltroTarea.entries.forEach { filtro ->
            FilterChip(
                selected = filtroActual == filtro,
                onClick  = { onFiltroChange(filtro) },
                label    = {
                    Text(
                        when (filtro) {
                            FiltroTarea.TODAS       -> "Todas"
                            FiltroTarea.PENDIENTES  -> "Pendientes"
                            FiltroTarea.COMPLETADAS -> "Completadas"
                        }
                    )
                }
            )
        }
    }
}

@Composable
fun FilterChipHabits(
    filtroActual: FiltroHabit,
    onFiltroChange: (FiltroHabit) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FiltroHabit.entries.forEach { filtro ->
            FilterChip(
                selected = filtroActual == filtro,
                onClick  = { onFiltroChange(filtro) },
                label    = {
                    Text(
                        when (filtro) {
                            FiltroHabit.TODOS       -> "Todos"
                            FiltroHabit.COMPLETADOS -> "Completados"
                            FiltroHabit.PENDIENTES  -> "Pendientes"
                        }
                    )
                }
            )
        }
    }
}