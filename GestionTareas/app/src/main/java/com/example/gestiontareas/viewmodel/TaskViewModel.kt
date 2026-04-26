package com.example.gestiontareas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestiontareas.data.Prioridad
import com.example.gestiontareas.data.Task
import com.example.gestiontareas.data.TaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class FiltroTarea { TODAS, PENDIENTES, COMPLETADAS }

data class TaskUiState(
    val tareas: List<Task>     = emptyList(),
    val filtro: FiltroTarea    = FiltroTarea.TODAS,
    val darkTheme: Boolean     = false,
    val tareaEditando: Task?   = null,
    val mostrarDialog: Boolean = false
)

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    private val _filtro        = MutableStateFlow(FiltroTarea.TODAS)
    private val _darkTheme     = MutableStateFlow(false)
    private val _tareaEditando = MutableStateFlow<Task?>(null)
    private val _mostrarDialog = MutableStateFlow(false)
    private val _allTasks      = MutableStateFlow<List<Task>>(emptyList())

    init {
        viewModelScope.launch {
            repository.tasks.collect { _allTasks.value = it }
        }
    }

    val uiState: StateFlow<TaskUiState> = combine(
        _allTasks,
        _filtro,
        _darkTheme,
        _tareaEditando,
        _mostrarDialog
    ) { tareas, filtro, dark, editando, dialog ->
        val tareasFiltradas = when (filtro) {
            FiltroTarea.TODAS       -> tareas
            FiltroTarea.PENDIENTES  -> tareas.filter { !it.completada }
            FiltroTarea.COMPLETADAS -> tareas.filter { it.completada }
        }
        TaskUiState(tareasFiltradas, filtro, dark, editando, dialog)
    }.stateIn(
        scope         = viewModelScope,
        started       = SharingStarted.WhileSubscribed(5000),
        initialValue  = TaskUiState()
    )

    private fun saveTasks(tasks: List<Task>) {
        viewModelScope.launch { repository.saveTasks(tasks) }
    }

    fun agregarTarea(titulo: String, descripcion: String, prioridad: Prioridad) {
        val nuevas = _allTasks.value + Task(
            titulo      = titulo,
            descripcion = descripcion,
            prioridad   = prioridad
        )
        saveTasks(nuevas)
    }

    fun editarTarea(task: Task, titulo: String, descripcion: String, prioridad: Prioridad) {
        val actualizadas = _allTasks.value.map {
            if (it.id == task.id)
                it.copy(titulo = titulo, descripcion = descripcion, prioridad = prioridad)
            else it
        }
        saveTasks(actualizadas)
    }

    fun toggleCompletada(task: Task) {
        val actualizadas = _allTasks.value.map {
            if (it.id == task.id) it.copy(completada = !it.completada) else it
        }
        saveTasks(actualizadas)
    }

    fun eliminarTarea(task: Task) {
        val filtradas = _allTasks.value.filter { it.id != task.id }
        saveTasks(filtradas)
    }

    fun setFiltro(filtro: FiltroTarea)  { _filtro.value = filtro }
    fun toggleTheme()                   { _darkTheme.value = !_darkTheme.value }
    fun abrirDialog(task: Task? = null) { _tareaEditando.value = task; _mostrarDialog.value = true }
    fun cerrarDialog()                  { _tareaEditando.value = null; _mostrarDialog.value = false }
}