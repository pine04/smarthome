package au.edu.swin.sdmd.smarthome.ui.airconditioner

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.swin.sdmd.smarthome.AirConditionerTriggerEditDestination
import au.edu.swin.sdmd.smarthome.alarms.AirConditionerTriggerScheduler
import au.edu.swin.sdmd.smarthome.data.air_conditioner_trigger.AirConditionerTrigger
import au.edu.swin.sdmd.smarthome.data.air_conditioner_trigger.AirConditionerTriggerRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AirConditionerTriggerEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val airConditionerTriggerRepository: AirConditionerTriggerRepository,
    private val airConditionerTriggerScheduler: AirConditionerTriggerScheduler
) : ViewModel() {
    private val triggerId: Long =
        checkNotNull(savedStateHandle[AirConditionerTriggerEditDestination.triggerIdArg])

    private lateinit var originalTrigger: AirConditionerTrigger

    var uiState by mutableStateOf(
        AirConditionerTriggerEditUiState(
            AirConditionerTriggerDetails(
                airConditionerId = 0
            )
        )
    )
        private set

    init {
        viewModelScope.launch {
            originalTrigger =
                airConditionerTriggerRepository.getById(triggerId = triggerId).filterNotNull()
                    .first()
            Log.d("ORIGINAL_TRIGGER", originalTrigger.toString())
            uiState = AirConditionerTriggerEditUiState(originalTrigger.toDetails())
        }
    }

    fun updateAirConditionerTriggerDetails(airConditionerTriggerDetails: AirConditionerTriggerDetails) {
        Log.d("OLD_STATE", uiState.triggerDetails.toString())
        uiState = AirConditionerTriggerEditUiState(airConditionerTriggerDetails)
        Log.d("NEW_STATE", uiState.triggerDetails.toString())
    }

    suspend fun updateTrigger() {
        val trigger = uiState.triggerDetails.toAirConditionerTrigger()
        Log.d("UPDATE+AC+TRIGGER", trigger.toString())
        airConditionerTriggerRepository.update(trigger)

        airConditionerTriggerScheduler.cancel(originalTrigger)
        airConditionerTriggerScheduler.schedule(trigger)
    }

    suspend fun deleteTrigger() {
        airConditionerTriggerRepository.delete(originalTrigger)
        airConditionerTriggerScheduler.cancel(originalTrigger)
    }
}

data class AirConditionerTriggerEditUiState(
    val triggerDetails: AirConditionerTriggerDetails = AirConditionerTriggerDetails()
)

fun AirConditionerTrigger.toDetails(): AirConditionerTriggerDetails = AirConditionerTriggerDetails(
    triggerId = triggerId,
    airConditionerId = airConditionerId,
    triggerAction = action,
    triggerTime = time
)