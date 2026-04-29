package ni.edu.uam.taskflow.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun TaskFlowTheme(content: @Composable () -> Unit) {

    val darkTheme = isSystemInDarkTheme()

    val colors = if (darkTheme) darkColorScheme()
    else lightColorScheme()

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}