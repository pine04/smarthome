package au.edu.swin.sdmd.smarthome.ui.light

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.swin.sdmd.smarthome.LightEditDestination
import au.edu.swin.sdmd.smarthome.alarms.LightTriggerScheduler
import au.edu.swin.sdmd.smarthome.data.Room
import au.edu.swin.sdmd.smarthome.data.light.Light
import au.edu.swin.sdmd.smarthome.data.light.LightRepository
import au.edu.swin.sdmd.smarthome.data.light_trigger.LightTrigger
import au.edu.swin.sdmd.smarthome.data.light_trigger.LightTriggerRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// View model for the light edit screen.
class LightEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val lightRepository: LightRepository,
    private val lightTriggerRepository: LightTriggerRepository,
    private val lightTriggerScheduler: LightTriggerScheduler
) : ViewModel() {

    var lightEditUiState by mutableStateOf(LightEditUiState())
        private set

    var originalName by mutableStateOf("")
        private set

    private lateinit var originalLight: Light

    private val lightId: Int = checkNotNull(savedStateHandle[LightEditDestination.lightIdArg])

    init {
        viewModelScope.launch {
            originalLight = lightRepository.getById(lightId).filterNotNull().first()
            lightEditUiState = originalLight.toLightEditUiState()
            originalName = originalLight.name
        }
    }

    fun updateLightDetails(lightDetails: LightDetails) {
        lightEditUiState = LightEditUiState(lightDetails, isValid(lightDetails))
    }

    suspend fun updateLight() {
        lightRepository.update(lightEditUiState.lightDetails.toLight(originalLight))
    }

    suspend fun deleteLight() {
        val triggers = lightTriggerRepository.getAllForLight(lightId).filterNotNull().first()
        triggers.forEach { trigger ->
            lightTriggerScheduler.cancel(trigger)
            lightTriggerRepository.delete(trigger)
        }

        lightRepository.delete(originalLight)
    }

    private fun isValid(lightDetails: LightDetails): Boolean {
        return lightDetails.name.isNotBlank()
    }
}

data class LightEditUiState(
    val lightDetails: LightDetails = LightDetails(),
    val isValid: Boolean = false
)

data class LightDetails(
    val name: String = "",
    val location: Room = Room.LIVING_ROOM
)

fun Light.toLightEditUiState(): LightEditUiState = LightEditUiState(
    lightDetails = LightDetails(name = name, location = Room.entries.find { it.value == location } ?: Room.LIVING_ROOM),
    isValid = true
)

fun LightDetails.toLight(originalLight: Light): Light = originalLight.copy(
    name = name,
    location = location.value
)