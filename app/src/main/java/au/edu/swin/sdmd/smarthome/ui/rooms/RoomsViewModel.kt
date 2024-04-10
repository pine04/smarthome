package au.edu.swin.sdmd.smarthome.ui.rooms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditionerRepository
import au.edu.swin.sdmd.smarthome.data.light.LightRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

// View model for the Rooms screen.
class RoomsViewModel(
    private val lightRepository: LightRepository,
    private val airConditionerRepository: AirConditionerRepository
) : ViewModel() {
    val roomFlows = rooms.associate { room ->
        room.destination to getStateFlowForRoom(room.roomSearchArgument)
    }

    private fun getStateFlowForRoom(room: String): StateFlow<RoomDeviceCount> {
        return combine(
            lightRepository.getActiveCountByRoom(room),
            lightRepository.getCountByRoom(room),
            airConditionerRepository.getActiveCountByRoom(room),
            airConditionerRepository.getCountByRoom(room)
        ) { activeLights, lights, activeAirConditioners, airConditioners ->
            RoomDeviceCount(activeLights + activeAirConditioners, lights + airConditioners)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = RoomDeviceCount()
        )
    }
}

data class RoomDeviceCount(
    val active: Int = 0,
    val total: Int = 0
)