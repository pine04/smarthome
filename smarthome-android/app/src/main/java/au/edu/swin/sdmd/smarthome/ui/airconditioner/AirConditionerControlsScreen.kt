package au.edu.swin.sdmd.smarthome.ui.airconditioner

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.swin.sdmd.smarthome.R
import au.edu.swin.sdmd.smarthome.data.air_conditioner_trigger.AirConditionerAction
import au.edu.swin.sdmd.smarthome.data.airconditioner.FanSpeed
import au.edu.swin.sdmd.smarthome.ui.SmartHomeViewModelFactory
import au.edu.swin.sdmd.smarthome.ui.components.CheckboxOption
import au.edu.swin.sdmd.smarthome.ui.components.OnOffButton
import au.edu.swin.sdmd.smarthome.ui.components.TriggerCard
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

// Screen that allows the user to control an air conditioner.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirConditionerControlsScreen(
    navigateToAirConditionerEdit: (Int) -> Unit,
    navigateToAirConditionerTriggerAdd: (Int) -> Unit,
    navigateToAirConditionerTriggerEdit: (Long) -> Unit,
    showSnackbarMessage: suspend (String) -> Unit,
    viewModel: AirConditionerControlsViewModel = viewModel(factory = SmartHomeViewModelFactory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val airConditioner = uiState.airConditioner
    val triggers = uiState.airConditionerTriggers

    val coroutineScope = rememberCoroutineScope()
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")

    var temperature by remember(airConditioner.temperature) { mutableFloatStateOf(airConditioner.temperature) }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        item {
            Column(modifier = Modifier.semantics(mergeDescendants = true) { }) {
                Text(
                    text = airConditioner.name,
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = airConditioner.location,
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        item {
            OnOffButton(
                isOn = airConditioner.isOn,
                setIsOn = {
                    coroutineScope.launch {
                        viewModel.update(airConditioner.copy(isOn = !airConditioner.isOn))
                        showSnackbarMessage("Turned air conditioner ${if (!airConditioner.isOn) "on" else "off"}.")
                    }
                },
                modifier = Modifier.padding(vertical = 32.dp)
            )
        }

        item {
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
                            viewModel.update(airConditioner.copy(temperature = temperature))
                            showSnackbarMessage("Adjusted temperature to $temperature°C.")
                        }
                    },
                    modifier = Modifier.semantics {
                        stateDescription = "$temperature degrees Celsius."
                    }
                )
            }
        }

        item {
            Column(modifier = Modifier
                .padding(top = 32.dp)
                .semantics(mergeDescendants = true) {
                    stateDescription = "Fan speed ${airConditioner.fanSpeed}"
                }) {
                Text(
                    text = stringResource(R.string.fan_speed),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .clearAndSetSemantics { }
                )

                SingleChoiceSegmentedButtonRow {
                    FanSpeed.entries.forEachIndexed { index, speedOption ->
                        SegmentedButton(
                            selected = airConditioner.fanSpeed == speedOption,
                            onClick = {
                                coroutineScope.launch {
                                    viewModel.update(airConditioner.copy(fanSpeed = speedOption))
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
        }

        item {
            CheckboxOption(
                selected = airConditioner.isFavorite,
                optionText = stringResource(id = R.string.add_to_home_screen_for_quick_access),
                onClick = {
                    coroutineScope.launch {
                        viewModel.update(airConditioner.copy(isFavorite = !airConditioner.isFavorite))
                        showSnackbarMessage(
                            if (!airConditioner.isFavorite) {
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
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.triggers),
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = { navigateToAirConditionerTriggerAdd(airConditioner.id) }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        if (triggers.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.no_triggers_set),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
                )
            }
        } else {
            items(triggers) {trigger ->
                TriggerCard(
                    triggerActionNameResource = airConditionerActionToStringResource(trigger.action),
                    triggerTime = formatter.format(trigger.time),
                    navigateToTriggerEdit = { navigateToAirConditionerTriggerEdit(trigger.triggerId) },
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        item {
            Button(onClick = { navigateToAirConditionerEdit(airConditioner.id) }) {
                Text(
                    text = stringResource(R.string.edit_air_conditioner_information),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

private fun airConditionerActionToStringResource(airConditionerAction: AirConditionerAction): Int =
    when (airConditionerAction) {
        AirConditionerAction.TURN_ON -> R.string.action_turn_on
        AirConditionerAction.TURN_OFF -> R.string.action_turn_off
    }