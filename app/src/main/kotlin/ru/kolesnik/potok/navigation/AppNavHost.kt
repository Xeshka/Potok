package ru.kolesnik.potok.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import ru.kolesnik.potok.feature.lifearea.navigation.LifeAreaBaseRoute
import ru.kolesnik.potok.feature.lifearea.navigation.mainSection
import ru.kolesnik.potok.feature.task.navigation.navigateToTask
import ru.kolesnik.potok.feature.task.navigation.taskSection
import ru.kolesnik.potok.ui.AppState

@Composable
fun AppNavHost(
    appState: AppState,
    modifier: Modifier = Modifier,
    startDestination: Any = LifeAreaBaseRoute,
) {
    val navController = appState.navController
    
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        mainSection(
            onTaskClick = { taskId ->
                navController.navigateToTask(taskId)
            },
            taskDestination = {
                taskSection(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        )
    }
}