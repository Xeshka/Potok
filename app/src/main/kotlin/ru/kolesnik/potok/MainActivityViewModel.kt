package ru.kolesnik.potok

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kolesnik.potok.core.analytics.AnalyticsHelper
import ru.kolesnik.potok.core.data.repository.SyncRepository
import ru.kolesnik.potok.core.network.result.Result
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val syncRepository: SyncRepository,
    private val analyticsHelper: AnalyticsHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainActivityUiState())
    val uiState: StateFlow<MainActivityUiState> = _uiState.asStateFlow()

    init {
        initializeApp()
    }

    private fun initializeApp() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                // Выполняем начальную синхронизацию данных
                when (val result = syncRepository.syncAll()) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isInitialized = true
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Ошибка инициализации: ${result.exception.message}"
                        )
                    }
                    is Result.Loading -> {
                        // Уже показываем загрузку
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Ошибка инициализации: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun retry() {
        initializeApp()
    }
}

data class MainActivityUiState(
    val isLoading: Boolean = false,
    val isInitialized: Boolean = false,
    val error: String? = null
)

data class UserData(
    val themeBrand: ThemeBrand = ThemeBrand.DEFAULT,
    val darkThemeConfig: DarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
    val useDynamicColor: Boolean = false,
    val shouldHideOnboarding: Boolean = false,
)

enum class ThemeBrand {
    DEFAULT,
    ANDROID
}

enum class DarkThemeConfig {
    FOLLOW_SYSTEM,
    LIGHT,
    DARK
}