package ni.edu.uam.taskflow.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import ni.edu.uam.taskflow.model.*

enum class FilterType {
    ALL, COMPLETED, PENDING
}

class TaskViewModel : ViewModel() {

    private var nextId = 1

    private val _tasks = mutableStateListOf<Task>()
    val tasks: List<Task> = _tasks

    var searchQuery by mutableStateOf("")
        private set

    var filterType by mutableStateOf(FilterType.ALL)
        private set

    fun onSearchChange(query: String) {
        searchQuery = query
    }

    fun setFilter(filter: FilterType) {
        filterType = filter
    }

    val filteredTasks: List<Task>
        get() = tasks
            .filter { it.title.contains(searchQuery, true) }
            .filter {
                when (filterType) {
                    FilterType.ALL -> true
                    FilterType.COMPLETED -> it.isCompleted
                    FilterType.PENDING -> !it.isCompleted
                }
            }

    val completedCount: Int
        get() = tasks.count { it.isCompleted }

    fun addTask(title: String, desc: String, priority: Priority, dueDate: Long?) {
        if (title.isBlank()) return
        _tasks.add(Task(nextId++, title, desc, false, priority, dueDate))
    }

    fun toggleTask(id: Int) {
        val index = _tasks.indexOfFirst { it.id == id }
        if (index != -1) {
            _tasks[index] = _tasks[index].copy(
                isCompleted = !_tasks[index].isCompleted
            )
        }
    }

    fun deleteTask(id: Int) {
        _tasks.removeAll { it.id == id }
    }

    fun updateTask(id: Int, title: String, desc: String, priority: Priority, dueDate: Long?) {
        val index = _tasks.indexOfFirst { it.id == id }
        if (index != -1) {
            _tasks[index] = _tasks[index].copy(
                title = title,
                description = desc,
                priority = priority,
                dueDate = dueDate
            )
        }
    }

    fun getTask(id: Int): Task? {
        return _tasks.find { it.id == id }
    }
}