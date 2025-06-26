package ru.kolesnik.potok.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import ru.kolesnik.potok.feature.lifearea.navigation.LifeAreaRoute
import ru.kolesnik.potok.feature.lifearea.navigation.mainSection
import ru.kolesnik.potok.feature.task.navigation.taskSection
import ru.kolesnik.potok.ui.AppState

/**
 * Top-level navigation graph. Navigation is organized as a single activity with
 * composable destinations.
 */
@Composable
fun AppNavHost(
    appState: AppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    startDestination: Any = LifeAreaRoute,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        mainSection(
            onTaskClick = appState::navigateToTask,
            taskDestination = {
                taskSection(
                    onBackClick = navController::popBackStack,
                )
            }
        )
    }
}