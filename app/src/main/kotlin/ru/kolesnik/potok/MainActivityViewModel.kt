package ru.kolesnik.potok

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(MainActivityUiState())
    val uiState: StateFlow<MainActivityUiState> = _uiState.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        // Имитируем загрузку приложения
        viewModelScope.launch {
            delay(1500) // Показываем splash screen 1.5 секунды
            _isLoading.value = false
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
    
    /**
     * Переключение темной темы
     */
    fun toggleDarkTheme() {
        _uiState.value = _uiState.value.copy(
            darkTheme = !_uiState.value.darkTheme
        )
    }
    
    /**
     * Переключение динамических цветов
     */
    fun toggleDynamicColor() {
        _uiState.value = _uiState.value.copy(
            useDynamicColor = !_uiState.value.useDynamicColor
        )
    }
    
    /**
     * Установка темы
     */
    fun setDarkTheme(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(darkTheme = enabled)
    }
    
    /**
     * Установка динамических цветов
     */
    fun setDynamicColor(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(useDynamicColor = enabled)
    }
}