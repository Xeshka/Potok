package ru.kolesnik.potok.feature.lifearea.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import ru.kolesnik.potok.feature.lifearea.LifeAreaScreen

@Serializable
data object LifeAreaRoute

@Serializable
data object LifeAreaBaseRoute

fun NavGraphBuilder.mainSection(
    onTaskClick: (String) -> Unit,
    taskDestination: NavGraphBuilder.() -> Unit
) {
    navigation<LifeAreaBaseRoute>(startDestination = LifeAreaRoute) {
        composable<LifeAreaRoute> {
            LifeAreaScreen(onTaskClick)
        }
    }
    taskDestination()
}