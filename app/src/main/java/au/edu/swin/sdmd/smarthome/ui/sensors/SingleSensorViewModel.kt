package au.edu.swin.sdmd.smarthome.ui.sensors

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.swin.sdmd.smarthome.SensorDestination
import au.edu.swin.sdmd.smarthome.data.SensorData
import au.edu.swin.sdmd.smarthome.data.SensorRepository
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
import kotlin.reflect.KClass

// View model for the Single sensor screen.
class SingleSensorViewModel(
    savedStateHandle: SavedStateHandle,
    temperatureRepository: SensorRepository,
    humidityRepository: SensorRepository,
    lightSensorRepository: SensorRepository
) : ViewModel() {
    val sensorType: String = savedStateHandle[SensorDestination.sensorArg] ?: "temp"

    val listData = arrayListOf<Point>(Point(0f,0f))

    private fun getDataFlow(
        temperatureRepository: SensorRepository,
        humidityRepository: SensorRepository,
        lightSensorRepository: SensorRepository
    ) : StateFlow<SensorData> {
        var repo = temperatureRepository
        when(sensorType) {
            "temp" -> repo = temperatureRepository
            "humi" -> repo = humidityRepository
            "light" -> repo = lightSensorRepository
        }
        return repo.getValue().map { it ->
            listData.add(
                Point(
                    listData[listData.size-1].x - 1f,
                    it.value.toFloat()
                )
            )
            if(listData.size > 20) {
                listData.removeAt(0)
            }
            it
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = SensorData()
        )
    }

    val dataFlow : StateFlow<SensorData> = getDataFlow(
        temperatureRepository,
        humidityRepository,
        lightSensorRepository
    )
}