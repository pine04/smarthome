package au.edu.swin.sdmd.smarthome.ui.light

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.swin.sdmd.smarthome.LightTriggerEditDestination
import au.edu.swin.sdmd.smarthome.alarms.LightTriggerScheduler
import au.edu.swin.sdmd.smarthome.data.light_trigger.LightTrigger
import au.edu.swin.sdmd.smarthome.data.light_trigger.LightTriggerRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LightTriggerEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val lightTriggerRepository: LightTriggerRepository,
    private val lightTriggerScheduler: LightTriggerScheduler
) : ViewModel() {
    private val triggerId: Long = checkNotNull(savedStateHandle[LightTriggerEditDestination.triggerIdArg])

    private lateinit var originalTrigger: LightTrigger

    var uiState by mutableStateOf(LightTriggerEditUiState(LightTriggerDetails(lightId = 0)))
        private set

    init {
        viewModelScope.launch {
            originalTrigger = lightTriggerRepository.getById(triggerId = triggerId).filterNotNull().first()
            uiState = LightTriggerEditUiState(originalTrigger.toDetails())
        }
    }

    fun updateLightTriggerDetails(lightTriggerDetails: LightTriggerDetails) {
        uiState = LightTriggerEditUiState(lightTriggerDetails)
    }

    suspend fun updateTrigger() {
        val trigger = uiState.triggerDetails.toLightTrigger()
        lightTriggerRepository.update(trigger)

        lightTriggerScheduler.cancel(originalTrigger)
        lightTriggerScheduler.schedule(trigger)
    }

    suspend fun deleteTrigger() {
        lightTriggerRepository.delete(originalTrigger)
        lightTriggerScheduler.cancel(originalTrigger)
    }
}

data class LightTriggerEditUiState(
    val triggerDetails: LightTriggerDetails = LightTriggerDetails()
)

fun LightTrigger.toDetails(): LightTriggerDetails = LightTriggerDetails(
    triggerId = triggerId,
    lightId = lightId,
    triggerAction = action,
    triggerTime = time
)