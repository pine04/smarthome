package au.edu.swin.sdmd.smarthome.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.swin.sdmd.smarthome.data.SensorData
import au.edu.swin.sdmd.smarthome.data.SensorRepository
import au.edu.swin.sdmd.smarthome.data.SmartHomePreferences
import au.edu.swin.sdmd.smarthome.data.UserPreferencesRepository
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditioner
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditionerRepository
import au.edu.swin.sdmd.smarthome.data.light.Light
import au.edu.swin.sdmd.smarthome.data.light.LightRepository
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
    temperatureRepository: SensorRepository,
    humidityRepository: SensorRepository,
    lightSensorRepository: SensorRepository,
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

//    val listTemp = arrayListOf<Point>(Point(0f,0f))
//    val listHumi = arrayListOf<Point>(Point(0f,0f))
//    val listLight = arrayListOf<Point>(Point(0f,0f))

    val temperature: StateFlow<SensorData> = temperatureRepository.getValue().map { it ->
//        listTemp.add(
//            Point(
//                listTemp[listTemp.size-1].x - 1f,
//                it.value.toFloat()
//            )
//        )
//        if(listTemp.size > 20) {
//            listTemp.removeAt(0)
//        }
        it
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = SensorData()
    )

    val humidity: StateFlow<SensorData> = humidityRepository.getValue().map { it ->
//        listHumi.add(
//            Point(
//                listHumi[listHumi.size-1].x - 1f,
//                it.value.toFloat()
//            )
//        )
//        if(listHumi.size > 20) {
//            listHumi.removeAt(0)
//        }
        it
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = SensorData()
    )

    val light: StateFlow<SensorData> = lightSensorRepository.getValue().map { it ->
//        listLight.add(
//            Point(
//                listLight[listLight.size-1].x - 1f,
//                it.value.toFloat()
//            )
//        )
//        if(listLight.size > 20) {
//            listLight.removeAt(0)
//        }
        it
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = SensorData()
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
    val favoriteAirConditioners: List<AirConditioner> = listOf(),
)