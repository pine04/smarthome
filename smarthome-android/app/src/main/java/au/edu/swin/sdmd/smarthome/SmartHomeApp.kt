package au.edu.swin.sdmd.smarthome

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.swin.sdmd.smarthome.data.UserPreferencesRepository
import au.edu.swin.sdmd.smarthome.ui.SmartHomeViewModelFactory
import au.edu.swin.sdmd.smarthome.ui.theme.AppTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun SmartHomeApp(
    appThemeViewModel: AppThemeViewModel = viewModel(factory = SmartHomeViewModelFactory)
) {
    val darkModeOption = appThemeViewModel.darkModeState.collectAsState().value

    AppTheme(darkModeOption = darkModeOption) {
        SmartHomeNavGraph()
    }
}

// View model that contains the global dark mode settings.
class AppThemeViewModel(
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val darkModeState: StateFlow<String> = userPreferencesRepository.preferences.map { preferences ->
        preferences.darkMode
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = "auto"
    )
}