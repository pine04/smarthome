package au.edu.swin.sdmd.smarthome.ui.light

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import au.edu.swin.sdmd.smarthome.MainApplication
import au.edu.swin.sdmd.smarthome.data.light.Light
import au.edu.swin.sdmd.smarthome.data.light.LightRepository
import au.edu.swin.sdmd.smarthome.ui.home.LightItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun LightsScreen(
    navigateToLightControls: (Int) -> Unit,
    viewModel: LightsViewModel = viewModel(factory = LightsViewModel.Factory)
) {
    val lightsUiState by viewModel.lightsUiState.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(lightsUiState.lights) { light ->
            LightItem(
                light = light,
                navigateToLightControls = navigateToLightControls
            )
        }
    }
}

class LightsViewModel(
    private val lightsRepository: LightRepository
) : ViewModel() {
    val lightsUiState: StateFlow<LightsUiState> = lightsRepository.getAllLights().map { lights ->
        LightsUiState(lights)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = LightsUiState()
    )

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val container = (this[APPLICATION_KEY] as MainApplication).container
                val lightRepository = container.lightRepository
                LightsViewModel(lightRepository)
            }
        }
    }
}

data class LightsUiState(
    val lights: List<Light> = listOf()
)