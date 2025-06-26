package ru.kolesnik.potok

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel() {
    
    val uiState: StateFlow<MainActivityUiState> = flowOf(
        UserData(
            darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
            useDynamicColor = true,
            shouldHideOnboarding = true
        )
    ).map(MainActivityUiState::Success)
        .stateIn(
            scope = viewModelScope,
            initialValue = MainActivityUiState.Loading,
            started = SharingStarted.WhileSubscribed(5_000),
        )
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val userData: UserData) : MainActivityUiState
}

data class UserData(
    val darkThemeConfig: DarkThemeConfig,
    val useDynamicColor: Boolean,
    val shouldHideOnboarding: Boolean,
)

enum class DarkThemeConfig {
    FOLLOW_SYSTEM,
    LIGHT,
    DARK,
}