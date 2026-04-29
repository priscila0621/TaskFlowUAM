package ni.edu.uam.taskflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ni.edu.uam.taskflow.model.Priority
import ni.edu.uam.taskflow.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(nav: NavController, vm: TaskViewModel, id: Int) {

    val task = vm.getTask(id)

    if (task == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text("Tarea no encontrada")
        }
        return
    }

    var title by remember { mutableStateOf(task.title) }
    var desc by remember { mutableStateOf(task.description) }
    var priority by remember { mutableStateOf(task.priority) }
    var dueDate by remember { mutableStateOf(task.dueDate) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showConfirm by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // 🔥 VALIDACIÓN
    fun isValidDate(selectedDate: Long?): Boolean {
        if (selectedDate == null) return true

        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        return selectedDate >= today
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Editar tarea") },
                colors = centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize() // 🔥 IMPORTANTE
                .background(MaterialTheme.colorScheme.background) // 🔥 FIX REAL
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = desc,
                onValueChange = { desc = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Text("Prioridad")

            Row {
                Priority.values().forEach {
                    FilterChip(
                        selected = priority == it,
                        onClick = { priority = it },
                        label = { Text(it.displayName()) },
                        modifier = Modifier.padding(end = 6.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Button(onClick = { showDatePicker = true }) {
                Text("Seleccionar fecha")
            }

            dueDate?.let {
                Text("Fecha: ${formatter.format(Date(it))}")
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    if (!isValidDate(dueDate)) {
                        showError = true
                        return@Button
                    }
                    showConfirm = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar cambios")
            }
        }
    }

    // 📅 DATE PICKER
    if (showDatePicker) {
        val dateState = rememberDatePickerState(
            initialSelectedDateMillis = dueDate
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(onClick = {
                    dueDate = dateState.selectedDateMillis
                    showDatePicker = false
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = dateState)
        }
    }

    // ❌ ERROR
    if (showError) {
        AlertDialog(
            onDismissRequest = { showError = false },
            confirmButton = {
                Button(onClick = { showError = false }) {
                    Text("OK")
                }
            },
            title = { Text("Error") },
            text = { Text("La fecha no puede ser pasada ❌") }
        )
    }

    // ✅ CONFIRMACIÓN
    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            confirmButton = {
                Button(onClick = {
                    vm.updateTask(id, title, desc, priority, dueDate)
                    nav.popBackStack()
                }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) {
                    Text("Cancelar")
                }
            },
            title = { Text("Confirmación") },
            text = { Text("¿Deseas guardar los cambios?") }
        )
    }
}