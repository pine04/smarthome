package au.edu.swin.sdmd.smarthome.ui.light

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.swin.sdmd.smarthome.LightControlsDestination
import au.edu.swin.sdmd.smarthome.data.light.Light
import au.edu.swin.sdmd.smarthome.data.light.LightRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

// View model for the light controls screen.
class LightControlsViewModel(
    savedStateHandle: SavedStateHandle,
    private val lightRepository: LightRepository
) : ViewModel() {
    private val lightId: Int = checkNotNull(savedStateHandle[LightControlsDestination.lightIdArg])

    val uiState: StateFlow<Light> = lightRepository.getById(lightId).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = Light(name = "My Light", location = "Living room")
    )

    suspend fun updateLight(light: Light) {
        lightRepository.update(light)
    }
}