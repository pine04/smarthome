package au.edu.swin.sdmd.smarthome.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import au.edu.swin.sdmd.smarthome.data.airconditioner.OfflineAirConditionerRepository
import au.edu.swin.sdmd.smarthome.data.light.OfflineLightRepository

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_preferences"
)

class AppContainer(private val context: Context) {
    val airConditionerRepository by lazy {
        OfflineAirConditionerRepository(SmartHomeDatabase.getDatabase(context).airConditionerDao())
    }

    val lightRepository by lazy {
        OfflineLightRepository(SmartHomeDatabase.getDatabase(context).lightDao())
    }

    val userPreferencesRepository by lazy {
        UserPreferencesRepository(context.dataStore)
    }
}