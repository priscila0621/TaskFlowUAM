package ni.edu.uam.taskflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ni.edu.uam.taskflow.navigation.NavGraph
import ni.edu.uam.taskflow.ui.theme.TaskFlowTheme
import ni.edu.uam.taskflow.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {

    // 🔥 ViewModel único para toda la app
    private val vm = TaskViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TaskFlowTheme {
                NavGraph(vm)
            }
        }
    }
}