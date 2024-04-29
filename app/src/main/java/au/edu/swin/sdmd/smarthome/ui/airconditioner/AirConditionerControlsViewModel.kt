package au.edu.swin.sdmd.smarthome.ui.airconditioner

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.swin.sdmd.smarthome.AirConditionerControlsDestination
import au.edu.swin.sdmd.smarthome.data.air_conditioner_trigger.AirConditionerTrigger
import au.edu.swin.sdmd.smarthome.data.air_conditioner_trigger.AirConditionerTriggerRepository
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditioner
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditionerRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

// View model for air conditioner control screen.
class AirConditionerControlsViewModel(
    savedStateHandle: SavedStateHandle,
    private val airConditionerRepository: AirConditionerRepository,
    airConditionerTriggerRepository: AirConditionerTriggerRepository
) : ViewModel() {
    private val airConditionerId: Int =
        checkNotNull(savedStateHandle[AirConditionerControlsDestination.airConditionerIdArg])

    val uiState: StateFlow<AirConditionerControlsUiState> = combine(
        airConditionerRepository.getById(airConditionerId),
        airConditionerTriggerRepository.getAllForAirConditioner(airConditionerId)
    ) { airConditioner, triggers ->
        if (airConditioner == null) {
            AirConditionerControlsUiState()
        } else {
            AirConditionerControlsUiState(airConditioner, triggers)
        }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = AirConditionerControlsUiState()
    )

    suspend fun update(airConditioner: AirConditioner) {
        airConditionerRepository.update(airConditioner)
    }
}

data class AirConditionerControlsUiState(
    val airConditioner: AirConditioner = AirConditioner(name = "Air conditioner", location = "Living room"),
    val airConditionerTriggers: List<AirConditionerTrigger> = listOf()
)