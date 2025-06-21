package ru.kolesnik.potok.feature.task.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import ru.kolesnik.potok.feature.task.TaskDetailScreen

@Serializable
data class TaskDetailViewRoute(
    val taskId: String
)

@Serializable
data object TaskBaseRoute

fun NavController.navigateToTask(taskId: String) =
    navigate(route = TaskDetailViewRoute(taskId))

fun NavGraphBuilder.taskSection(
    onBackClick: () -> Unit,
) {
    composable<TaskDetailViewRoute> {
        TaskDetailScreen(onBackClick)
    }
}
