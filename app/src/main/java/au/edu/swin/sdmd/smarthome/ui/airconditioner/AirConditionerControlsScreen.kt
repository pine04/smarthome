package au.edu.swin.sdmd.smarthome.ui.airconditioner

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.swin.sdmd.smarthome.R
import au.edu.swin.sdmd.smarthome.data.airconditioner.FanSpeed
import au.edu.swin.sdmd.smarthome.ui.SmartHomeViewModelFactory
import au.edu.swin.sdmd.smarthome.ui.components.CheckboxOption
import au.edu.swin.sdmd.smarthome.ui.components.OnOffButton
import kotlinx.coroutines.launch

// Screen that allows the user to control an air conditioner.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirConditionerControlsScreen(
    navigateToAirConditionerEdit: (Int) -> Unit,
    showSnackbarMessage: suspend (String) -> Unit,
    viewModel: AirConditionerControlsViewModel = viewModel(factory = SmartHomeViewModelFactory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var temperature by remember(uiState.temperature) { mutableFloatStateOf(uiState.temperature) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier.semantics(mergeDescendants = true) { }) {
            Text(
                text = uiState.name,
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = uiState.location,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }

        OnOffButton(
            isOn = uiState.isOn,
            setIsOn = {
                coroutineScope.launch {
                    viewModel.update(uiState.copy(isOn = !uiState.isOn))
                    showSnackbarMessage("Turned air conditioner ${if (!uiState.isOn) "on" else "off"}.")
                }
            },
            modifier = Modifier.padding(vertical = 32.dp)
        )

        Column(modifier = Modifier.semantics(mergeDescendants = true) { }) {
            Text(
                text = stringResource(R.string.temperature_c, temperature),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Slider(
                value = temperature,
                valueRange = 18f..30f,
                steps = 23,
                onValueChange = { temperature = it },
                onValueChangeFinished = {
                    coroutineScope.launch {
                        viewModel.update(uiState.copy(temperature = temperature))
                        showSnackbarMessage("Adjusted temperature to $temperature°C.")
                    }
                },
                modifier = Modifier.semantics {
                    stateDescription = "$temperature degrees Celsius."
                }
            )
        }

        Column(modifier = Modifier.padding(top = 32.dp).semantics(mergeDescendants = true) { stateDescription = "Fan speed ${uiState.fanSpeed}" }) {
            Text(
                text = stringResource(R.string.fan_speed),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .clearAndSetSemantics {  }
            )

            SingleChoiceSegmentedButtonRow {
                FanSpeed.entries.forEachIndexed { index, speedOption ->
                    SegmentedButton(
                        selected = uiState.fanSpeed == speedOption,
                        onClick = {
                            coroutineScope.launch {
                                viewModel.update(uiState.copy(fanSpeed = speedOption))
                                showSnackbarMessage("Changed fan speed to ${speedOption.value}.")
                            }
                        },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = FanSpeed.entries.size
                        )
                    ) {
                        Text(
                            text = speedOption.value,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        CheckboxOption(
            selected = uiState.isFavorite,
            optionText = stringResource(id = R.string.add_to_home_screen_for_quick_access),
            onClick = {
                coroutineScope.launch {
                    viewModel.update(uiState.copy(isFavorite = !uiState.isFavorite))
                    showSnackbarMessage(
                        if (!uiState.isFavorite) {
                            "Added air conditioner to home screen."
                        } else {
                            "Removed air conditioner from home screen."
                        }
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp)
        )

        Button(onClick = { navigateToAirConditionerEdit(uiState.id) }) {
            Text(
                text = stringResource(R.string.edit_air_conditioner_information),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}