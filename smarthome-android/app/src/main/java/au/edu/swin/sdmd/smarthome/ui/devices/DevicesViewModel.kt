package au.edu.swin.sdmd.smarthome.ui.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditionerRepository
import au.edu.swin.sdmd.smarthome.data.light.LightRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

// View model for the device screen.
class DevicesViewModel(
    lightRepository: LightRepository,
    airConditionerRepository: AirConditionerRepository
) : ViewModel() {
    val uiState: StateFlow<DevicesScreenUiState> = combine(
        lightRepository.getCount(),
        lightRepository.getActiveCount(),
        airConditionerRepository.getCount(),
        airConditionerRepository.getActiveCount()
    ) { lights, activeLights, airConditioners, activeAirConditioners ->
        DevicesScreenUiState(lights, activeLights, airConditioners, activeAirConditioners)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = DevicesScreenUiState()
    )
}

data class DevicesScreenUiState(
    val allLights: Int = 0,
    val allActiveLights: Int = 0,
    val allAirConditioners: Int = 0,
    val allActiveAirConditioners: Int = 0
)