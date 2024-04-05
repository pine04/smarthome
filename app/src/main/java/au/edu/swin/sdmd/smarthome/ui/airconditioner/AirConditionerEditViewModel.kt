package au.edu.swin.sdmd.smarthome.ui.airconditioner

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.swin.sdmd.smarthome.AirConditionerEditDestination
import au.edu.swin.sdmd.smarthome.data.Room
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditioner
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditionerRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// View model for the air conditioner screen.
class AirConditionerEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val airConditionerRepository: AirConditionerRepository
) : ViewModel() {
    var uiState by mutableStateOf(AirConditionerEditUiState())
        private set

    var originalName by mutableStateOf("")
        private set

    private lateinit var originalAirConditioner: AirConditioner

    private val airConditionerId: Int = checkNotNull(savedStateHandle[AirConditionerEditDestination.airConditionerIdArg])

    init {
        viewModelScope.launch {
            originalAirConditioner = airConditionerRepository.getById(airConditionerId).filterNotNull().first()
            uiState = originalAirConditioner.toAirConditionerEditUiState()
            originalName = originalAirConditioner.name
        }
    }

    fun updateAirConditionerDetails(details: AirConditionerDetails) {
        uiState = AirConditionerEditUiState(details, isValid(details))
    }

    suspend fun update() {
        airConditionerRepository.update(uiState.details.toAirConditioner(originalAirConditioner))
    }

    suspend fun delete() {
        airConditionerRepository.delete(originalAirConditioner)
    }

    private fun isValid(details: AirConditionerDetails): Boolean {
        return details.name.isNotBlank()
    }
}

data class AirConditionerEditUiState(
    val details: AirConditionerDetails = AirConditionerDetails(),
    val isValid: Boolean = false
)

data class AirConditionerDetails(
    val name: String = "",
    val location: Room = Room.LIVING_ROOM
)

fun AirConditioner.toAirConditionerEditUiState(): AirConditionerEditUiState = AirConditionerEditUiState(
    details = AirConditionerDetails(
        name = name,
        location = Room.entries.find { it.value == location } ?: Room.LIVING_ROOM
    ),
    isValid = true
)

fun AirConditionerDetails.toAirConditioner(originalAirConditioner: AirConditioner): AirConditioner = originalAirConditioner.copy(
    name = name,
    location = location.value
)