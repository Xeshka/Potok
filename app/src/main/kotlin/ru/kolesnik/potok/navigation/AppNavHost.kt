package ru.kolesnik.potok.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ru.kolesnik.potok.feature.lifearea.navigation.LifeAreaBaseRoute
import ru.kolesnik.potok.feature.lifearea.navigation.mainSection
import ru.kolesnik.potok.feature.task.navigation.navigateToTask
import ru.kolesnik.potok.feature.task.navigation.taskSection

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = LifeAreaBaseRoute,
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