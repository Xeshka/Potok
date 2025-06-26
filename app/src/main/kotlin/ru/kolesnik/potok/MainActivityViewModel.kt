package ru.kolesnik.potok

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<MainActivityUiState>(MainActivityUiState.Loading)
    val uiState: StateFlow<MainActivityUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Симулируем загрузку пользовательских настроек
            // В реальном приложении здесь будет загрузка из DataStore
            _uiState.value = MainActivityUiState.Success(
                userData = UserData(
                    themeBrand = ThemeBrand.DEFAULT,
                    useDarkTheme = false,
                    useDynamicColor = false
                )
            )
        }
    }
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val userData: UserData) : MainActivityUiState
}

data class UserData(
    val themeBrand: ThemeBrand,
    val useDarkTheme: Boolean,
    val useDynamicColor: Boolean,
)

enum class ThemeBrand {
    DEFAULT,
    ANDROID,
}