package au.edu.swin.sdmd.smarthome.ui.rooms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.swin.sdmd.smarthome.SingleRoomDestination
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditioner
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditionerRepository
import au.edu.swin.sdmd.smarthome.data.light.Light
import au.edu.swin.sdmd.smarthome.data.light.LightRepository
import au.edu.swin.sdmd.smarthome.ui.SmartHomeViewModelFactory
import au.edu.swin.sdmd.smarthome.ui.home.AirConditionerItem
import au.edu.swin.sdmd.smarthome.ui.home.LightItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@Composable
fun SingleRoomScreen(
    navigateToLightControls: (Int) -> Unit,
    navigateToAirConditionerControls: (Int) -> Unit,
    viewModel: SingleRoomViewModel = viewModel(factory = SmartHomeViewModelFactory)
) {
    val uiState = viewModel.uiState.collectAsState()
    val lights = uiState.value.lights
    val airConditioners = uiState.value.airConditioners

    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(text = "Lights")
        }

        items(lights) { light ->
            LightItem(
                light = light,
                navigateToLightControls = navigateToLightControls
            )
        }

        item {
            Text(text = "Air conditioners")
        }

        items(airConditioners) { airConditioner ->
            AirConditionerItem(
                airConditioner = airConditioner,
                navigateToAirConditionerControls = navigateToAirConditionerControls
            )
        }
    }
}

class SingleRoomViewModel(
    savedStateHandle: SavedStateHandle,
    lightRepository: LightRepository,
    airConditionerRepository: AirConditionerRepository
) : ViewModel() {
    private val room: String = checkNotNull(savedStateHandle[SingleRoomDestination.roomArg])

    val uiState: StateFlow<SingleRoomUiState> = combine(
        lightRepository.getAllForRoom(room),
        airConditionerRepository.getAllForRoom(room)
    ) { lights, airConditioners ->
        SingleRoomUiState(lights, airConditioners)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = SingleRoomUiState()
    )
}

data class SingleRoomUiState(
    val lights: List<Light> = listOf(),
    val airConditioners: List<AirConditioner> = listOf()
)