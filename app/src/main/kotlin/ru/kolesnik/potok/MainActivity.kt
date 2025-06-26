package ru.kolesnik.potok

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import ru.kolesnik.potok.navigation.AppNavHost
import ru.kolesnik.potok.ui.AppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        
        // Настройка splash screen
        splashScreen.setKeepOnScreenCondition {
            viewModel.isLoading.value
        }
        
        enableEdgeToEdge()
        
        setContent {
            val uiState by viewModel.uiState.collectAsState()
            
            AppTheme(
                darkTheme = uiState.darkTheme,
                disableDynamicTheming = !uiState.useDynamicColor
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost()
                }
            }
        }
    }
}

/**
 * Состояние UI главной активности
 */
data class MainActivityUiState(
    val darkTheme: Boolean = false,
    val useDynamicColor: Boolean = true,
    val isLoading: Boolean = true
)