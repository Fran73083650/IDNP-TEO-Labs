package com.example.gestiontareas.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gestiontareas.data.HabitRepository
import com.example.gestiontareas.data.TaskRepository
import com.example.gestiontareas.ui.screens.HabitDetailScreen
import com.example.gestiontareas.ui.screens.HabitScreen
import com.example.gestiontareas.ui.screens.HomeScreen
import com.example.gestiontareas.ui.screens.TaskScreen
import com.example.gestiontareas.viewmodel.HabitViewModel
import com.example.gestiontareas.viewmodel.HabitViewModelFactory
import com.example.gestiontareas.viewmodel.TaskViewModel
import com.example.gestiontareas.viewmodel.TaskViewModelFactory

// Sealed class para rutas — buena práctica
sealed class Screen(val route: String) {
    object Home        : Screen("home")
    object Tasks       : Screen("tasks")
    object Habits      : Screen("habits")
    object HabitDetail : Screen("habit_detail/{habitId}") {
        fun createRoute(habitId: Int) = "habit_detail/$habitId"
    }
}

@Composable
fun AppNavigation(darkTheme: Boolean, onToggleTheme: () -> Unit) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val taskViewModel: TaskViewModel = viewModel(
        factory = TaskViewModelFactory(TaskRepository(context))
    )
    val habitViewModel: HabitViewModel = viewModel(
        factory = HabitViewModelFactory(HabitRepository(context))
    )

    NavHost(navController = navController, startDestination = Screen.Home.route) {

        composable(Screen.Home.route) {
            HomeScreen(
                darkTheme     = darkTheme,
                onToggleTheme = onToggleTheme,
                onGoToTasks   = { navController.navigate(Screen.Tasks.route) },
                onGoToHabits  = { navController.navigate(Screen.Habits.route) }
            )
        }

        composable(Screen.Tasks.route) {
            TaskScreen(
                viewModel = taskViewModel,
                onBack    = { navController.popBackStack() }
            )
        }

        composable(Screen.Habits.route) {
            HabitScreen(
                viewModel      = habitViewModel,
                onBack         = { navController.popBackStack() },
                onHabitClick   = { habitId ->
                    navController.navigate(Screen.HabitDetail.createRoute(habitId))
                }
            )
        }

        composable(
            route = Screen.HabitDetail.route,
            arguments = listOf(navArgument("habitId") { type = NavType.IntType })
        ) { backStackEntry ->
            val habitId = backStackEntry.arguments?.getInt("habitId") ?: return@composable
            HabitDetailScreen(
                habitId   = habitId,
                viewModel = habitViewModel,
                onBack    = { navController.popBackStack() }
            )
        }
    }
}