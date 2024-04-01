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

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val DARK_MODE = stringPreferencesKey("dark_mode")
    }

    val darkMode: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e("UserPreferencesRepository", "An error happened while reading preferences.", it)
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[DARK_MODE] ?: "auto"
        }

    suspend fun saveDarkModePreferences(darkMode: String) {
        if (!allowedDarkModeOptions.contains(darkMode)) {
            throw IllegalArgumentException("Dark mode option '$darkMode' is not allowed.")
        }

        dataStore.edit { preferences ->
            preferences[DARK_MODE] = darkMode
        }
    }
}