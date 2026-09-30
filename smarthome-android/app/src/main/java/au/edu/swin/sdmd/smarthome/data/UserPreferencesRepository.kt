package au.edu.swin.sdmd.smarthome.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val allowedDarkModeOptions = arrayOf("auto", "light", "dark")

// A repository which reads from Preferences DataStore. Used by ViewModels to access dark mode and username information.
class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        const val LOG_TAG = "UserPreferencesRepository"
        val DARK_MODE = stringPreferencesKey("dark_mode")
        val USERNAME = stringPreferencesKey("username")
    }

    val preferences: Flow<SmartHomePreferences> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(LOG_TAG, "An error happened while reading preferences.", it)
            }
        }
        .map { preferences ->
            SmartHomePreferences(
                darkMode = preferences[DARK_MODE] ?: "auto",
                username = preferences[USERNAME] ?: "User"
            )
        }

    suspend fun saveDarkModePreferences(darkMode: String) {
        if (!allowedDarkModeOptions.contains(darkMode)) {
            throw IllegalArgumentException("Dark mode option '$darkMode' is not allowed.")
        }

        dataStore.edit { preferences ->
            preferences[DARK_MODE] = darkMode
        }
    }

    suspend fun saveUsernamePreferences(username: String) {
        if (username.isEmpty()) {
            throw IllegalArgumentException("Empty username.")
        }

        dataStore.edit { preferences ->
            preferences[USERNAME] = username
        }
    }
}

data class SmartHomePreferences(
    val darkMode: String = "auto",
    val username: String = "User"
)