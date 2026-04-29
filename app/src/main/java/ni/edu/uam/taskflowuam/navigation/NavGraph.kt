package ni.edu.uam.taskflow.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import ni.edu.uam.taskflow.ui.screens.*
import ni.edu.uam.taskflow.viewmodel.TaskViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(vm: TaskViewModel) {

    val navController = rememberNavController()

    NavHost(navController, startDestination = "home") {

        composable("home") {
            HomeScreen(navController, vm)
        }

        composable("add") {
            AddTaskScreen(navController, vm)
        }

        composable("detail/{id}") {
            val id = it.arguments?.getString("id")?.toInt() ?: 0
            DetailScreen(navController, vm, id)
        }

        composable("edit/{id}") {
            val id = it.arguments?.getString("id")?.toInt() ?: 0
            EditTaskScreen(navController, vm, id)
        }
    }
}