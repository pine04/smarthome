package au.edu.swin.sdmd.smarthome.ui.airconditioner

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.swin.sdmd.smarthome.AirConditionerControlsDestination
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditioner
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditionerRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

// View model for air conditioner control screen.
class AirConditionerControlsViewModel(
    savedStateHandle: SavedStateHandle,
    private val airConditionerRepository: AirConditionerRepository
) : ViewModel() {
    private val airConditionerId: Int =
        checkNotNull(savedStateHandle[AirConditionerControlsDestination.airConditionerIdArg])

    val uiState: StateFlow<AirConditioner> =
        airConditionerRepository.getById(airConditionerId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AirConditioner(name = "My AC", location = "Living room")
        )

    suspend fun update(airConditioner: AirConditioner) {
        airConditionerRepository.update(airConditioner)
    }
}