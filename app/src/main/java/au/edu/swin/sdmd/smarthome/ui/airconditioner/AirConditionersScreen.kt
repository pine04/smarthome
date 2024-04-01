package au.edu.swin.sdmd.smarthome.ui.airconditioner

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
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditioner
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditionerRepository
import au.edu.swin.sdmd.smarthome.data.light.Light
import au.edu.swin.sdmd.smarthome.data.light.LightRepository
import au.edu.swin.sdmd.smarthome.ui.home.AirConditionerItem
import au.edu.swin.sdmd.smarthome.ui.home.LightItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun AirConditionersScreen(
    navigateToAirConditionerControls: (Int) -> Unit,
    viewModel: AirConditionersViewModel = viewModel(factory = AirConditionersViewModel.Factory)
) {
    val airConditionersUiState by viewModel.airConditionersUiState.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(airConditionersUiState.airConditioners) { airConditioner ->
            AirConditionerItem(
                airConditioner = airConditioner,
                navigateToAirConditionerControls = navigateToAirConditionerControls
            )
        }
    }
}

class AirConditionersViewModel(
    private val airConditionerRepository: AirConditionerRepository
) : ViewModel() {
    val airConditionersUiState: StateFlow<AirConditionersUiState> = airConditionerRepository.getAllAirConditioners().map { airConditioners ->
        AirConditionersUiState(airConditioners)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = AirConditionersUiState()
    )

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val container = (this[APPLICATION_KEY] as MainApplication).container
                val airConditionerRepository = container.airConditionerRepository
                AirConditionersViewModel(airConditionerRepository)
            }
        }
    }
}

data class AirConditionersUiState(
    val airConditioners: List<AirConditioner> = listOf()
)