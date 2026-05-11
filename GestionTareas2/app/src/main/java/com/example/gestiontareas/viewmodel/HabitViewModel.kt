package com.example.gestiontareas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestiontareas.data.Habit
import com.example.gestiontareas.data.HabitRepository
import com.example.gestiontareas.data.completadoHoy
import com.example.gestiontareas.data.fechaHoy
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class FiltroHabit { TODOS, COMPLETADOS, PENDIENTES }

data class HabitUiState(
    val habitos: List<Habit>         = emptyList(),
    val todosLosHabitos: List<Habit> = emptyList(),
    val filtro: FiltroHabit          = FiltroHabit.TODOS
)

class HabitViewModel(private val repository: HabitRepository) : ViewModel() {

    private val _filtro    = MutableStateFlow(FiltroHabit.TODOS)
    private val _allHabits = MutableStateFlow<List<Habit>>(emptyList())

    init {
        viewModelScope.launch {
            repository.habits.collect { _allHabits.value = it }
        }
    }

    val uiState: StateFlow<HabitUiState> = combine(
        _allHabits,
        _filtro
    ) { habitos, filtro ->
        val filtrados = when (filtro) {
            FiltroHabit.TODOS       -> habitos
            FiltroHabit.COMPLETADOS -> habitos.filter { it.completadoHoy() }
            FiltroHabit.PENDIENTES  -> habitos.filter { !it.completadoHoy() }
        }
        HabitUiState(filtrados, habitos, filtro)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HabitUiState())

    private fun save(habits: List<Habit>) {
        viewModelScope.launch { repository.saveHabits(habits) }
    }

    fun agregarHabit(nombre: String, descripcion: String, color: String) {
        save(_allHabits.value + Habit(nombre = nombre, descripcion = descripcion, color = color))
    }

    fun toggleHoy(habit: Habit) {
        val hoy = fechaHoy()
        val historial = if (habit.historial.contains(hoy))
            habit.historial - hoy
        else
            habit.historial + hoy
        val actualizados = _allHabits.value.map {
            if (it.id == habit.id) it.copy(historial = historial) else it
        }
        save(actualizados)
    }

    fun eliminarHabit(habit: Habit) {
        save(_allHabits.value.filter { it.id != habit.id })
    }

    fun setFiltro(filtro: FiltroHabit) { _filtro.value = filtro }
}