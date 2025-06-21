package ru.kolesnik.potok.feature.lifearea

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import ru.kolesnik.potok.core.designsystem.AppTheme
import ru.kolesnik.potok.core.ui.AppHeader
import ru.kolesnik.potok.core.ui.LifeAreaItem

@Preview
@Composable
fun LifeAreaScreenPreview() {
    AppTheme {
        LifeAreaScreen({})
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun LifeAreaScreen(
    onTaskClick: (String) -> Unit,
    viewModel: LifeAreaViewModel = hiltViewModel()
) {
    val lifeAreas by viewModel.lifeAreas.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val flows by viewModel.flows.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val scope = rememberCoroutineScope()

    val totalPages = runCatching { lifeAreas.size }.getOrDefault(0)
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { totalPages }
    )
    Scaffold(
        topBar = {
            AppHeader(
                onBackClick = {
                    scope.launch {
                        pagerState.scrollToPage(0)
                    }
                },
                rightContent = {
                    Text(
                        text = "${pagerState.currentPage + 1} из $totalPages",
                        fontSize = 24.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            )
        }
    ) { innerPadding ->
        when {
            error != null -> ErrorMessage(error!!)
            isLoading -> LoadingIndicator()
            lifeAreas.isEmpty() -> NoDataMessage()
            else -> {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.padding(innerPadding)
                ) { page ->
                    LifeAreaItem(
                        onTaskClick = onTaskClick,
                        lifeArea = lifeAreas[page],
                        taskMains = tasks[lifeAreas[page].id],
                        flows = flows[lifeAreas[page].id] ?: emptyList(),
                        onTaskDelete = viewModel::deleteTask,
                        onCloseClick = viewModel::closeTask,
                        onCreateClick = viewModel::createTask,
                    )
                }
            }
        }
    }
}
