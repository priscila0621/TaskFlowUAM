package ni.edu.uam.taskflow.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ni.edu.uam.taskflow.model.Priority
import ni.edu.uam.taskflow.viewmodel.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(nav: NavController, vm: TaskViewModel) {

    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("TaskFlowUAM") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                nav.navigate("add")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->

        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = vm.searchQuery,
                onValueChange = { vm.onSearchChange(it) },
                label = { Text("Buscar tarea") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Row {
                FilterType.values().forEach {

                    val label = when (it) {
                        FilterType.ALL -> "Todas"
                        FilterType.COMPLETED -> "Completadas"
                        FilterType.PENDING -> "Pendientes"
                    }

                    FilterChip(
                        selected = vm.filterType == it,
                        onClick = { vm.setFilter(it) },
                        label = { Text(label) },
                        modifier = Modifier.padding(end = 6.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Text("Completadas: ${vm.completedCount}")

            Spacer(Modifier.height(12.dp))

            if (vm.filteredTasks.isEmpty()) {
                Text("No hay tareas aún 📝")
            }

            LazyColumn {
                items(vm.filteredTasks) { task ->

                    val color = when (task.priority) {
                        Priority.HIGH -> MaterialTheme.colorScheme.error
                        Priority.MEDIUM -> MaterialTheme.colorScheme.primary
                        Priority.LOW -> MaterialTheme.colorScheme.tertiary
                    }

                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                nav.navigate("detail/${task.id}")
                            }
                    ) {
                        Column(Modifier.padding(16.dp)) {

                            Text(task.title, color = color)

                            Text(task.description)

                            task.dueDate?.let {
                                Text("Entrega: ${formatter.format(Date(it))}")
                            }

                            Row {
                                Checkbox(
                                    checked = task.isCompleted,
                                    onCheckedChange = {
                                        vm.toggleTask(task.id)
                                    }
                                )
                                Text(if (task.isCompleted) "Completada" else "Pendiente")
                            }
                        }
                    }
                }
            }
        }
    }
}