package ni.edu.uam.taskflow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ni.edu.uam.taskflow.model.Priority
import ni.edu.uam.taskflow.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(nav: NavController, vm: TaskViewModel) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(Priority.MEDIUM) }
    var dueDate by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // 🔥 VALIDACIÓN DE FECHA
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
        topBar = {
            TopAppBar(title = { Text("Nueva tarea") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
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

            Spacer(Modifier.height(12.dp))

            Button(onClick = {

                // 🔥 VALIDACIÓN NUEVA (TÍTULO)
                if (title.isBlank()) {
                    scope.launch {
                        snackbarHostState.showSnackbar("El título es obligatorio ❌")
                    }
                    return@Button
                }

                // 🔥 VALIDACIÓN DE FECHA
                if (!isValidDate(dueDate)) {
                    scope.launch {
                        snackbarHostState.showSnackbar("La fecha no puede ser pasada ❌")
                    }
                    return@Button
                }

                // ✅ SOLO SI TODO ESTÁ BIEN
                vm.addTask(title, desc, priority, dueDate)

                scope.launch {
                    snackbarHostState.showSnackbar("Tarea guardada ✔")
                }

                nav.popBackStack()

            }) {
                Text("Guardar")
            }
        }
    }

    // 📅 DATE PICKER
    if (showDatePicker) {
        val dateState = rememberDatePickerState()

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
}