package au.edu.swin.sdmd.smarthome.ui.light

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.swin.sdmd.smarthome.data.light.Light
import au.edu.swin.sdmd.smarthome.data.light.LightRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

// View model for the Lights screen.
class LightsViewModel(
    private val lightRepository: LightRepository
) : ViewModel() {
    val lightsUiState: StateFlow<LightsUiState> = lightRepository.getAll().map { lights ->
        LightsUiState(lights)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = LightsUiState()
    )

    suspend fun updateLight(light: Light) {
        lightRepository.update(light)
    }
}

data class LightsUiState(
    val lights: List<Light> = listOf()
)