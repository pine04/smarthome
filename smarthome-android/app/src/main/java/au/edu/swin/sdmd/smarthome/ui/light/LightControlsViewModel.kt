package au.edu.swin.sdmd.smarthome.ui.light

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.swin.sdmd.smarthome.LightControlsDestination
import au.edu.swin.sdmd.smarthome.data.light.Light
import au.edu.swin.sdmd.smarthome.data.light.LightRepository
import au.edu.swin.sdmd.smarthome.data.light_trigger.LightTrigger
import au.edu.swin.sdmd.smarthome.data.light_trigger.LightTriggerRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

// View model for the light controls screen.
class LightControlsViewModel(
    savedStateHandle: SavedStateHandle,
    private val lightRepository: LightRepository,
    lightTriggerRepository: LightTriggerRepository
) : ViewModel() {
    private val lightId: Int = checkNotNull(savedStateHandle[LightControlsDestination.lightIdArg])

    val uiState: StateFlow<LightControlsUiState> = combine(
        lightRepository.getById(lightId),
        lightTriggerRepository.getAllForLight(lightId)
    ) { light, triggers ->
        if (light == null) {
            LightControlsUiState()
        } else {
            LightControlsUiState(light, triggers)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = LightControlsUiState()
    )

    suspend fun updateLight(light: Light) {
        lightRepository.update(light)
    }
}

data class LightControlsUiState(
    val light: Light = Light(name = "Light", location = "Living room"),
    val lightTriggers: List<LightTrigger> = listOf()
)