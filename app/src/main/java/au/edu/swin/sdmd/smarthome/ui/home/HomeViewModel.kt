package au.edu.swin.sdmd.smarthome.ui.home

import android.util.Log
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
import co.yml.charts.common.model.Point
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

    val listTemp = arrayListOf<Point>(Point(0f,0f))
    val listHumi = arrayListOf<Point>(Point(0f,0f))
    val listLight = arrayListOf<Point>(Point(0f,0f))

    val temperature: StateFlow<TemperatureData> = temperatureRepository.getTemperature().map { it ->
        listTemp.add(
            Point(
                listTemp[listTemp.size-1].x - 1f,
                it.temperature.toFloat()
            )
        )
        if(listTemp.size > 20) {
            listTemp.removeAt(0)
        }
        it
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = TemperatureData()
    )

    val humidity: StateFlow<HumidityData> = humidityRepository.getHumidity().map { it ->
        listHumi.add(
            Point(
                listHumi[listHumi.size-1].x - 1f,
                it.humidity.toFloat()
            )
        )
        if(listHumi.size > 20) {
            listHumi.removeAt(0)
        }
        it
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HumidityData()
    )

    val light: StateFlow<LightData> = lightSensorRepository.getLight().map { it ->
        listLight.add(
            Point(
                listLight[listLight.size-1].x - 1f,
                it.light.toFloat()
            )
        )
        if(listLight.size > 20) {
            listLight.removeAt(0)
        }
        it
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = LightData()
    )


    fun getLineChartData(listSize: Int, start: Int = 0, maxRange: Int): List<Point> {
        val list = arrayListOf<Point>()
        for (index in 0 until listSize) {
            list.add(
                Point(
                    index.toFloat(),
                    (start until maxRange).random().toFloat()
                )
            )
        }
        return list
    }

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
    val favoriteAirConditioners: List<AirConditioner> = listOf(),
)