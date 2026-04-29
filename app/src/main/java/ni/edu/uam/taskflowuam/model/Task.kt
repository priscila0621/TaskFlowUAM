package ni.edu.uam.taskflow.model

enum class Priority {
    HIGH, MEDIUM, LOW;

    fun displayName(): String {
        return when (this) {
            HIGH -> "Alta"
            MEDIUM -> "Media"
            LOW -> "Baja"
        }
    }
}

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val priority: Priority = Priority.MEDIUM,
    val dueDate: Long? = null
)