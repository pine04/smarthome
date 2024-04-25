package au.edu.swin.sdmd.smarthome.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import au.edu.swin.sdmd.smarthome.data.airconditioner.OfflineAirConditionerRepository
import au.edu.swin.sdmd.smarthome.data.light.OfflineLightRepository
import au.edu.swin.sdmd.smarthome.data.sensor_humidity.MQTTHumidityRepository
import au.edu.swin.sdmd.smarthome.data.sensor_light.MQTTLightRepository
import au.edu.swin.sdmd.smarthome.data.sensor_temperature.MQTTTemperatureRepository
import kotlinx.coroutines.CoroutineScope

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_preferences"
)

// Contains the repositories used by the ViewModels. This container is used for dependency injection.
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

    private val sensorDataSource = SensorDataSource(context)

    val temperatureRepository by lazy {
        MQTTTemperatureRepository(sensorDataSource)
    }

    val humidityRepository by lazy {
        MQTTHumidityRepository(sensorDataSource)
    }

    val lightSensorRepository by lazy {
        MQTTLightRepository(sensorDataSource)
    }
}