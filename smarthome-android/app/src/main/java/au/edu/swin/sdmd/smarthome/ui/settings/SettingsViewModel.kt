package au.edu.swin.sdmd.smarthome.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.swin.sdmd.smarthome.data.UserPreferencesRepository
import au.edu.swin.sdmd.smarthome.data.allowedDarkModeOptions
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// View model for the Settings screen.
class SettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    var darkModeSettings by mutableStateOf("auto")
    var usernameSettings by mutableStateOf("User")

    init {
        viewModelScope.launch {
            val preferences = userPreferencesRepository.preferences.first()

            darkModeSettings = if (!allowedDarkModeOptions.contains(preferences.darkMode)) {
                "auto"
            } else {
                preferences.darkMode
            }

            usernameSettings = preferences.username.ifBlank {
                "User"
            }
        }
    }

    fun isUsernameValid(username: String): Boolean {
        return username.isNotBlank()
    }

    suspend fun updateDarkModeSettings() {
        if (allowedDarkModeOptions.contains(darkModeSettings)) {
            userPreferencesRepository.saveDarkModePreferences(darkModeSettings)
        }
    }

    suspend fun updateUsername() {
        if (isUsernameValid(usernameSettings)) {
            userPreferencesRepository.saveUsernamePreferences(usernameSettings)
        }
    }
}