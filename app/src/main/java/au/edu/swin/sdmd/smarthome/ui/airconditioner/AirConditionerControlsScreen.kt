package au.edu.swin.sdmd.smarthome.ui.airconditioner

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import au.edu.swin.sdmd.smarthome.AirConditionerControlsDestination
import au.edu.swin.sdmd.smarthome.MainApplication
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditioner
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditionerRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.swin.sdmd.smarthome.data.airconditioner.FanSpeed
import au.edu.swin.sdmd.smarthome.ui.light.OnOffButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirConditionerControlsScreen(
    navigateToAirConditionerEdit: (Int) -> Unit,
    viewModel: AirConditionerControlsViewModel = viewModel(factory = AirConditionerControlsViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var temperature by remember { mutableFloatStateOf(uiState.temperature) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = uiState.toString())

        OnOffButton(
            isOn = uiState.isOn,
            setIsOn = { coroutineScope.launch { viewModel.update(uiState.copy(isOn = !uiState.isOn)) } }
        )

        Slider(
            value = temperature,
            valueRange = 18f..30f,
            steps = 23,
            onValueChange = { temperature = it },
            onValueChangeFinished = {
                coroutineScope.launch {
                    viewModel.update(uiState.copy(temperature = temperature))
                }
            }
        )

        SingleChoiceSegmentedButtonRow {
            FanSpeed.entries.forEachIndexed { index, speedOption ->
                SegmentedButton(
                    selected = uiState.fanSpeed == speedOption,
                    onClick = {
                        coroutineScope.launch {
                            viewModel.update(uiState.copy(fanSpeed = speedOption))
                        }
                    },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = FanSpeed.entries.size)
                ) {
                    Text(speedOption.value)
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = uiState.isFavorite,
                onCheckedChange = {
                    coroutineScope.launch {
                        viewModel.update(uiState.copy(isFavorite = !uiState.isFavorite))
                    }
                }
            )
            Text("Add to home screen for quick access")
        }

        Button(onClick = { navigateToAirConditionerEdit(uiState.id) }) {
            Text(text = "Edit air conditioner information")
        }
    }
}

class AirConditionerControlsViewModel(
    savedStateHandle: SavedStateHandle,
    private val airConditionerRepository: AirConditionerRepository
) : ViewModel() {
    private val airConditionerId: Int =
        checkNotNull(savedStateHandle[AirConditionerControlsDestination.airConditionerIdArg])

    val uiState: StateFlow<AirConditioner> =
        airConditionerRepository.getAirConditionerById(airConditionerId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AirConditioner(name = "My AC", location = "Living room")
        )

    suspend fun update(airConditioner: AirConditioner) {
        airConditionerRepository.update(airConditioner)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val container = (this[APPLICATION_KEY] as MainApplication).container
                val airConditionerRepository = container.airConditionerRepository
                AirConditionerControlsViewModel(
                    this.createSavedStateHandle(),
                    airConditionerRepository
                )
            }
        }
    }
}