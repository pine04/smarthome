package au.edu.swin.sdmd.smarthome.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import au.edu.swin.sdmd.smarthome.alarms.AirConditionerTriggerScheduler
import au.edu.swin.sdmd.smarthome.alarms.LightTriggerScheduler
import au.edu.swin.sdmd.smarthome.data.air_conditioner_trigger.OfflineAirConditionerTriggerRepository
import au.edu.swin.sdmd.smarthome.data.airconditioner.OfflineAirConditionerRepository
import au.edu.swin.sdmd.smarthome.data.light.OfflineLightRepository
import au.edu.swin.sdmd.smarthome.data.light_trigger.OfflineLightTriggerRepository
import au.edu.swin.sdmd.smarthome.data.sensor_humidity.MQTTHumiditySensorRepository
import au.edu.swin.sdmd.smarthome.data.sensor_light.MQTTLightSensorRepository
import au.edu.swin.sdmd.smarthome.data.sensor_temperature.MQTTTemperatureSensorRepository

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

    val temperatureSensorRepository by lazy {
        MQTTTemperatureSensorRepository(sensorDataSource)
    }

    val humiditySensorRepository by lazy {
        MQTTHumiditySensorRepository(sensorDataSource)
    }

    val lightSensorRepository by lazy {
        MQTTLightSensorRepository(sensorDataSource)
    }

    val lightTriggerRepository by lazy {
        OfflineLightTriggerRepository(SmartHomeDatabase.getDatabase(context).lightTriggerDao())
    }

    val lightTriggerScheduler by lazy {
        LightTriggerScheduler(context)
    }

    val airConditionerTriggerRepository by lazy {
        OfflineAirConditionerTriggerRepository(SmartHomeDatabase.getDatabase(context).airConditionerTriggerDao())
    }

    val airConditionerTriggerScheduler by lazy {
        AirConditionerTriggerScheduler(context)
    }
}