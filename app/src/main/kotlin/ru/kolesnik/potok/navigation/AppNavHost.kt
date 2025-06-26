package ru.kolesnik.potok.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import ru.kolesnik.potok.feature.lifearea.navigation.LifeAreaRoute
import ru.kolesnik.potok.feature.lifearea.navigation.mainSection
import ru.kolesnik.potok.feature.task.navigation.navigateToTask
import ru.kolesnik.potok.feature.task.navigation.taskSection

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LifeAreaRoute
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