package au.edu.swin.sdmd.smarthome.ui.sensors

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.swin.sdmd.smarthome.SensorDestination
import au.edu.swin.sdmd.smarthome.data.SensorData
import au.edu.swin.sdmd.smarthome.data.SensorRepository
import co.yml.charts.common.model.Point
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

// View model for the Single sensor screen.
class SingleSensorViewModel(
    savedStateHandle: SavedStateHandle,
    temperatureRepository: SensorRepository,
    humidityRepository: SensorRepository,
    lightSensorRepository: SensorRepository
) : ViewModel() {
    val sensorType: String = savedStateHandle[SensorDestination.sensorArg] ?: "temp"

    val listData = arrayListOf(Point(0f,0f))

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

    val isOn = when (sensorType) {
        "humi" -> humidityRepository.getStatus()
        "light" -> lightSensorRepository.getStatus()
        else -> temperatureRepository.getStatus()
    }.map { it }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = true
    )

    val turnOnSensor: () -> Unit = when (sensorType) {
        "humi" -> humidityRepository::turnOnSensor
        "light" -> lightSensorRepository::turnOnSensor
        else -> temperatureRepository::turnOnSensor
    }

    val turnOffSensor: () -> Unit = when (sensorType) {
        "humi" -> humidityRepository::turnOffSensor
        "light" -> lightSensorRepository::turnOffSensor
        else -> temperatureRepository::turnOffSensor
    }

    val dataFlow : StateFlow<SensorData> = getDataFlow(
        temperatureRepository,
        humidityRepository,
        lightSensorRepository
    )
}