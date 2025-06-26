package ru.kolesnik.potok.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import ru.kolesnik.potok.feature.lifearea.navigation.LifeAreaRoute
import ru.kolesnik.potok.feature.lifearea.navigation.mainSection
import ru.kolesnik.potok.feature.task.navigation.navigateToTask
import ru.kolesnik.potok.feature.task.navigation.taskSection

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    onShowSnackbar: suspend (message: String, action: String?) -> Boolean
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LifeAreaRoute,
        modifier = modifier
    ) {
        mainSection(
            onTaskClick = { taskId ->
                navController.navigateToTask(taskId)
            }
        ) {
            taskSection(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}