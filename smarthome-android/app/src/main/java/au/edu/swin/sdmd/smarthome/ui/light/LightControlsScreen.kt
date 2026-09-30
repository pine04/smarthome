package au.edu.swin.sdmd.smarthome.ui.light

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.swin.sdmd.smarthome.R
import au.edu.swin.sdmd.smarthome.data.light_trigger.LightAction
import au.edu.swin.sdmd.smarthome.ui.SmartHomeViewModelFactory
import au.edu.swin.sdmd.smarthome.ui.components.CheckboxOption
import au.edu.swin.sdmd.smarthome.ui.components.OnOffButton
import au.edu.swin.sdmd.smarthome.ui.components.TriggerCard
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

// Screen where the user can control a light.
@Composable
fun LightControlsScreen(
    navigateToLightEdit: (Int) -> Unit,
    navigateToLightTriggerAdd: (Int) -> Unit,
    navigateToLightTriggerEdit: (Long) -> Unit,
    showSnackbarMessage: suspend (String) -> Unit,
    viewModel: LightControlsViewModel = viewModel(factory = SmartHomeViewModelFactory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val light = uiState.light
    val triggers = uiState.lightTriggers

    val coroutineScope = rememberCoroutineScope()
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")

    var brightness by remember(light.brightness) { mutableFloatStateOf(light.brightness) }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
    ) {
        item {
            Column(modifier = Modifier.semantics(mergeDescendants = true) { }) {
                Text(
                    text = light.name,
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = light.location,
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        item {
            OnOffButton(
                isOn = light.isOn,
                setIsOn = {
                    coroutineScope.launch {
                        viewModel.updateLight(light.copy(isOn = !light.isOn))
                        showSnackbarMessage("Turned light ${if (!light.isOn) "on" else "off"}.")
                    }
                },
                modifier = Modifier.padding(vertical = 32.dp)
            )
        }

        item {
            Column(modifier = Modifier.semantics(mergeDescendants = true) { }) {
                Text(
                    text = stringResource(R.string.brightness),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.fillMaxWidth()
                )

                Slider(
                    value = brightness,
                    valueRange = 0f..1f,
                    steps = 8,
                    onValueChange = { brightness = it },
                    onValueChangeFinished = {
                        coroutineScope.launch {
                            viewModel.updateLight(light.copy(brightness = brightness))
                            showSnackbarMessage("Adjusted light brightness.")
                        }
                    }
                )
            }
        }

        item {
            CheckboxOption(
                selected = light.isFavorite,
                optionText = stringResource(R.string.add_to_home_screen_for_quick_access),
                onClick = {
                    coroutineScope.launch {
                        viewModel.updateLight(light.copy(isFavorite = !light.isFavorite))
                        showSnackbarMessage(
                            if (!light.isFavorite) {
                                "Added light to home screen."
                            } else {
                                "Removed light from home screen."
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

                IconButton(onClick = { navigateToLightTriggerAdd(light.id) }) {
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
                    triggerActionNameResource = lightActionToStringResource(trigger.action),
                    triggerTime = formatter.format(trigger.time),
                    navigateToTriggerEdit = { navigateToLightTriggerEdit(trigger.triggerId) },
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        item {
            Button(onClick = { navigateToLightEdit(light.id) }) {
                Text(
                    text = stringResource(R.string.edit_light_information),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

private fun lightActionToStringResource(lightAction: LightAction): Int =
    when (lightAction) {
        LightAction.TURN_ON -> R.string.action_turn_on
        LightAction.TURN_OFF -> R.string.action_turn_off
    }