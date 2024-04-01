package au.edu.swin.sdmd.smarthome.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.swin.sdmd.smarthome.data.UserPreferencesRepository
import au.edu.swin.sdmd.smarthome.data.allowedDarkModeOptions
import au.edu.swin.sdmd.smarthome.ui.SmartHomeViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = viewModel(factory = SmartHomeViewModelFactory)
) {
    val settingsUiState = viewModel.uiState
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text(text = "List of settings...")
        Text(text = settingsUiState.darkModeOption)

        TextField(value = settingsUiState.darkModeOption, onValueChange = { viewModel.updateUiState(settingsUiState.copy(darkModeOption = it)) })

        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.updateSettings()
                }
            }
        ) {
            Text("Update settings")
        }
    }
}

class SettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private lateinit var originalDarkModeSetting: String

    var uiState by mutableStateOf(SettingsUiState())
        private set

    fun updateUiState(state: SettingsUiState) {
        uiState = state
    }

    suspend fun updateSettings() {
        userPreferencesRepository.saveDarkModePreferences(uiState.darkModeOption)
    }

    init {
        viewModelScope.launch {
            originalDarkModeSetting = userPreferencesRepository.darkMode.first()
            uiState = SettingsUiState(
                if (!allowedDarkModeOptions.contains(originalDarkModeSetting)) {
                    "auto"
                } else {
                    originalDarkModeSetting
                }
            )
        }
    }
}

data class SettingsUiState(
    val darkModeOption: String = "auto"
)