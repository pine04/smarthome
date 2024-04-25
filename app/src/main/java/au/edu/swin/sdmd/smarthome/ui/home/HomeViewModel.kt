package au.edu.swin.sdmd.smarthome.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.swin.sdmd.smarthome.data.SensorRepository
import au.edu.swin.sdmd.smarthome.data.SmartHomePreferences
import au.edu.swin.sdmd.smarthome.data.UserPreferencesRepository
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditioner
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditionerRepository
import au.edu.swin.sdmd.smarthome.data.light.Light
import au.edu.swin.sdmd.smarthome.data.light.LightRepository
import au.edu.swin.sdmd.smarthome.data.sensor_humidity.HumidityData
import au.edu.swin.sdmd.smarthome.data.sensor_humidity.HumidityRepository
import au.edu.swin.sdmd.smarthome.data.sensor_light.LightData
import au.edu.swin.sdmd.smarthome.data.sensor_temperature.TemperatureData
import au.edu.swin.sdmd.smarthome.data.sensor_temperature.TemperatureRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

// View model for the home screen.
class HomeViewModel(
    private val lightRepository: LightRepository,
    private val airConditionerRepository: AirConditionerRepository,
    temperatureRepository: TemperatureRepository,
    humidityRepository: HumidityRepository,
    lightSensorRepository: au.edu.swin.sdmd.smarthome.data.sensor_light.LightRepository,
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

    val temperature: StateFlow<TemperatureData> = temperatureRepository.getTemperature().map { it }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = TemperatureData()
    )

    val humidity: StateFlow<HumidityData> = humidityRepository.getHumidity().map { it }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HumidityData()
    )

    val light: StateFlow<LightData> = lightSensorRepository.getLight().map { it }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = LightData()
    )

    suspend fun updateLight(light: Light) {
        lightRepository.update(light)
    }

    suspend fun updateAirConditioner(airConditioner: AirConditioner) {
        airConditionerRepository.update(airConditioner)
    }
}

data class HomeScreenUiState(
    val username: String = "User",
    val favoriteLights: List<Light> = listOf(),
    val favoriteAirConditioners: List<AirConditioner> = listOf()
)