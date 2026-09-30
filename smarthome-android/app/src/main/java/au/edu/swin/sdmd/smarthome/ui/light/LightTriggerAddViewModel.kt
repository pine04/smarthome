package au.edu.swin.sdmd.smarthome.ui.light

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import au.edu.swin.sdmd.smarthome.LightTriggerAddDestination
import au.edu.swin.sdmd.smarthome.alarms.LightTriggerScheduler
import au.edu.swin.sdmd.smarthome.data.light_trigger.LightAction
import au.edu.swin.sdmd.smarthome.data.light_trigger.LightTrigger
import au.edu.swin.sdmd.smarthome.data.light_trigger.LightTriggerRepository
import java.time.LocalTime

class LightTriggerAddViewModel(
    savedStateHandle: SavedStateHandle,
    private val lightTriggerRepository: LightTriggerRepository,
    private val lightTriggerScheduler: LightTriggerScheduler
) : ViewModel() {
    private val lightId: Int = checkNotNull(savedStateHandle[LightTriggerAddDestination.lightIdArg])

    var uiState by mutableStateOf(LightTriggerAddUiState(LightTriggerDetails(lightId = lightId)))
        private set

    fun updateLightTriggerDetails(lightTriggerDetails: LightTriggerDetails) {
        uiState = LightTriggerAddUiState(lightTriggerDetails)
    }

    suspend fun addTrigger() {
        val trigger = uiState.triggerDetails.toLightTrigger()
        lightTriggerRepository.insert(trigger)
        lightTriggerScheduler.schedule(trigger)
    }
}

data class LightTriggerAddUiState(
    val triggerDetails: LightTriggerDetails = LightTriggerDetails()
)

data class LightTriggerDetails(
    val triggerId: Long = 0,
    val lightId: Int = 0,
    val triggerAction: LightAction = LightAction.TURN_ON,
    val triggerTime: LocalTime = LocalTime.MIDNIGHT
)

fun LightTriggerDetails.toLightTrigger() = LightTrigger(
    triggerId = triggerId,
    lightId = lightId,
    action = triggerAction,
    time = triggerTime
)