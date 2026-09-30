package au.edu.swin.sdmd.smarthome.ui.airconditioner

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import au.edu.swin.sdmd.smarthome.AirConditionerTriggerAddDestination
import au.edu.swin.sdmd.smarthome.alarms.AirConditionerTriggerScheduler
import au.edu.swin.sdmd.smarthome.data.air_conditioner_trigger.AirConditionerAction
import au.edu.swin.sdmd.smarthome.data.air_conditioner_trigger.AirConditionerTrigger
import au.edu.swin.sdmd.smarthome.data.air_conditioner_trigger.AirConditionerTriggerRepository
import java.time.LocalTime

class AirConditionerTriggerAddViewModel(
    savedStateHandle: SavedStateHandle,
    private val airConditionerTriggerRepository: AirConditionerTriggerRepository,
    private val airConditionerTriggerScheduler: AirConditionerTriggerScheduler
) : ViewModel() {
    private val airConditionerId: Int = checkNotNull(savedStateHandle[AirConditionerTriggerAddDestination.airConditionerIdArg])

    var uiState by mutableStateOf(AirConditionerTriggerAddUiState(AirConditionerTriggerDetails(airConditionerId = airConditionerId)))
        private set

    fun updateAirConditionerTriggerDetails(airConditionerTriggerDetails: AirConditionerTriggerDetails) {
        uiState = AirConditionerTriggerAddUiState(airConditionerTriggerDetails)
    }

    suspend fun addTrigger() {
        val trigger = uiState.triggerDetails.toAirConditionerTrigger()
        airConditionerTriggerRepository.insert(trigger)
        airConditionerTriggerScheduler.schedule(trigger)
    }
}

data class AirConditionerTriggerAddUiState(
    val triggerDetails: AirConditionerTriggerDetails = AirConditionerTriggerDetails()
)

data class AirConditionerTriggerDetails(
    val triggerId: Long = 0,
    val airConditionerId: Int = 0,
    val triggerAction: AirConditionerAction = AirConditionerAction.TURN_ON,
    val triggerTime: LocalTime = LocalTime.MIDNIGHT
)

fun AirConditionerTriggerDetails.toAirConditionerTrigger() = AirConditionerTrigger(
    triggerId = triggerId,
    airConditionerId = airConditionerId,
    action = triggerAction,
    time = triggerTime
)