package au.edu.swin.sdmd.smarthome.ui.rooms

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.swin.sdmd.smarthome.SingleRoomDestination
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditioner
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditionerRepository
import au.edu.swin.sdmd.smarthome.data.light.Light
import au.edu.swin.sdmd.smarthome.data.light.LightRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

// View model for the Single room screen.
class SingleRoomViewModel(
    savedStateHandle: SavedStateHandle,
    private val lightRepository: LightRepository,
    private val airConditionerRepository: AirConditionerRepository
) : ViewModel() {
    private val room: String = checkNotNull(savedStateHandle[SingleRoomDestination.roomArg])
    private val roomSearchArgument: String = rooms.find { it.destination == room }?.roomSearchArgument ?: "Living Room"

    val uiState: StateFlow<SingleRoomUiState> = combine(
        lightRepository.getByRoom(roomSearchArgument),
        airConditionerRepository.getByRoom(roomSearchArgument)
    ) { lights, airConditioners ->
        SingleRoomUiState(lights, airConditioners)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = SingleRoomUiState()
    )

    suspend fun updateLight(light: Light) {
        lightRepository.update(light)
    }

    suspend fun updateAirConditioner(airConditioner: AirConditioner) {
        airConditionerRepository.update(airConditioner)
    }
}

data class SingleRoomUiState(
    val lights: List<Light> = listOf(),
    val airConditioners: List<AirConditioner> = listOf()
)