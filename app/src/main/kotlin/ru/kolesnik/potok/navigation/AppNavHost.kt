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
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = LifeAreaBaseRoute,
        modifier = modifier,
    ) {
        mainSection(
            onTaskClick = navController::navigateToTask
        ) {
            taskSection(
                onBackClick = navController::popBackStack
            )
        }
    }
}
