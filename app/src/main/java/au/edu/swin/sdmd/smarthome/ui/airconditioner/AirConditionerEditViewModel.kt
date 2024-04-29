package au.edu.swin.sdmd.smarthome.ui.airconditioner

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.swin.sdmd.smarthome.AirConditionerEditDestination
import au.edu.swin.sdmd.smarthome.alarms.AirConditionerTriggerScheduler
import au.edu.swin.sdmd.smarthome.data.Room
import au.edu.swin.sdmd.smarthome.data.air_conditioner_trigger.AirConditionerTrigger
import au.edu.swin.sdmd.smarthome.data.air_conditioner_trigger.AirConditionerTriggerRepository
import au.edu.swin.sdmd.smarthome.data.air_conditioner_trigger.OfflineAirConditionerTriggerRepository
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditioner
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditionerRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// View model for the air conditioner screen.
class AirConditionerEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val airConditionerRepository: AirConditionerRepository,
    private val airConditionerTriggerRepository: AirConditionerTriggerRepository,
    private val airConditionerTriggerScheduler: AirConditionerTriggerScheduler
) : ViewModel() {
    var airConditionerEditUiState by mutableStateOf(AirConditionerEditUiState())
        private set

    var originalName by mutableStateOf("")
        private set

    private lateinit var originalAirConditioner: AirConditioner

    private val airConditionerId: Int = checkNotNull(savedStateHandle[AirConditionerEditDestination.airConditionerIdArg])

    init {
        viewModelScope.launch {
            originalAirConditioner = airConditionerRepository.getById(airConditionerId).filterNotNull().first()
            airConditionerEditUiState = originalAirConditioner.toAirConditionerEditUiState()
            originalName = originalAirConditioner.name
        }
    }

    fun updateAirConditionerDetails(details: AirConditionerDetails) {
        airConditionerEditUiState = AirConditionerEditUiState(details, isValid(details))
    }

    suspend fun updateAirConditioner() {
        airConditionerRepository.update(airConditionerEditUiState.details.toAirConditioner(originalAirConditioner))
    }

    suspend fun deleteAirConditioner() {
        val triggers = airConditionerTriggerRepository.getAllForAirConditioner(airConditionerId).filterNotNull().first()
        triggers.forEach { trigger ->
            airConditionerTriggerScheduler.cancel(trigger)
            airConditionerTriggerRepository.delete(trigger)
        }

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