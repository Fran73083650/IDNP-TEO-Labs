package com.example.gestiontareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.gestiontareas.data.TaskRepository
import com.example.gestiontareas.ui.screens.TaskScreen
import com.example.gestiontareas.ui.theme.GestionTareasTheme
import com.example.gestiontareas.viewmodel.TaskViewModel
import com.example.gestiontareas.viewmodel.TaskViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(TaskRepository(applicationContext)) // ← sin DB, sin TaskDatabase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val state by viewModel.uiState.collectAsState()
            GestionTareasTheme(darkTheme = state.darkTheme) {
                TaskScreen(viewModel = viewModel)
            }
        }
    }
}