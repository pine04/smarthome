package au.edu.swin.sdmd.smarthome.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.swin.sdmd.smarthome.data.SensorData
import au.edu.swin.sdmd.smarthome.data.SensorRepository
import au.edu.swin.sdmd.smarthome.data.UserPreferencesRepository
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditioner
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditionerRepository
import au.edu.swin.sdmd.smarthome.data.light.Light
import au.edu.swin.sdmd.smarthome.data.light.LightRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

// View model for the home screen.
class HomeViewModel(
    private val lightRepository: LightRepository,
    private val airConditionerRepository: AirConditionerRepository,
    private val temperatureSensorRepository: SensorRepository,
    private val humiditySensorRepository: SensorRepository,
    private val lightSensorRepository: SensorRepository,
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val uiState: StateFlow<HomeScreenUiState> = combine(
        userPreferencesRepository.preferences,
        airConditionerRepository.getFavorites(),
        lightRepository.getFavorites()
    ) { preferences, airConditioners, lights ->
        HomeScreenUiState(
            username = preferences.username,
            favoriteAirConditioners = airConditioners,
            favoriteLights = lights
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HomeScreenUiState()
    )

    val temperature: StateFlow<SensorData> = temperatureSensorRepository.getValue().map {
        it
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = SensorData()
    )

    val humidity: StateFlow<SensorData> = humiditySensorRepository.getValue().map {
        it
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = SensorData()
    )

    val light: StateFlow<SensorData> = lightSensorRepository.getValue().map {
        it
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = SensorData()
    )

    val temperatureSensorStatus: StateFlow<Boolean> = temperatureSensorRepository.getStatus().map { it }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = true
    )

    val humiditySensorStatus: StateFlow<Boolean> = humiditySensorRepository.getStatus().map { it }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = true
    )

    val lightSensorStatus: StateFlow<Boolean> = lightSensorRepository.getStatus().map { it }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = true
    )

    suspend fun updateLight(light: Light) {
        lightRepository.update(light)
    }

    suspend fun updateAirConditioner(airConditioner: AirConditioner) {
        airConditionerRepository.update(airConditioner)
    }

    fun turnOnTemperatureSensor() {
        temperatureSensorRepository.turnOnSensor()
    }

    fun turnOffTemperatureSensor() {
        temperatureSensorRepository.turnOffSensor()
    }

    fun turnOnHumiditySensor() {
        humiditySensorRepository.turnOnSensor()
    }

    fun turnOffHumiditySensor() {
        humiditySensorRepository.turnOffSensor()
    }

    fun turnOnLightSensor() {
        lightSensorRepository.turnOnSensor()
    }

    fun turnOffLightSensor() {
        lightSensorRepository.turnOffSensor()
    }
}

data class HomeScreenUiState(
    val username: String = "User",
    val favoriteLights: List<Light> = listOf(),
    val favoriteAirConditioners: List<AirConditioner> = listOf()
)