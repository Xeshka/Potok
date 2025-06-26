package ru.kolesnik.potok.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ru.kolesnik.potok.feature.lifearea.navigation.LifeAreaRoute
import ru.kolesnik.potok.feature.lifearea.navigation.mainSection
import ru.kolesnik.potok.feature.task.navigation.navigateToTask
import ru.kolesnik.potok.feature.task.navigation.taskSection

@Composable
fun AppNavHost(
    navController: NavHostController,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    startDestination: Any = LifeAreaRoute,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        mainSection(
            onTaskClick = navController::navigateToTask,
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