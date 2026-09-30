package au.edu.swin.sdmd.smarthome.ui.sensors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.swin.sdmd.smarthome.data.SensorData
import au.edu.swin.sdmd.smarthome.data.SensorRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

// View model for the Sensors screen.
class SensorsViewModel(
    temperatureRepository: SensorRepository,
    humidityRepository: SensorRepository,
    lightSensorRepository: SensorRepository
) : ViewModel() {

    val temperature: StateFlow<SensorData> = temperatureRepository.getValue().map { it }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = SensorData()
    )

    val humidity: StateFlow<SensorData> = humidityRepository.getValue().map { it }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = SensorData()
    )

    val light: StateFlow<SensorData> = lightSensorRepository.getValue().map { it }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = SensorData()
    )
}