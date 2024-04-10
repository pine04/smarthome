package au.edu.swin.sdmd.smarthome.ui.light

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.swin.sdmd.smarthome.R
import au.edu.swin.sdmd.smarthome.ui.SmartHomeViewModelFactory
import au.edu.swin.sdmd.smarthome.ui.components.CheckboxOption
import au.edu.swin.sdmd.smarthome.ui.components.OnOffButton
import kotlinx.coroutines.launch

// Screen where the user can control a light.
@Composable
fun LightControlsScreen(
    navigateToLightEdit: (Int) -> Unit,
    showSnackbarMessage: suspend (String) -> Unit,
    viewModel: LightControlsViewModel = viewModel(factory = SmartHomeViewModelFactory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var brightness by remember(uiState.brightness) { mutableFloatStateOf(uiState.brightness) }

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
                    viewModel.updateLight(uiState.copy(isOn = !uiState.isOn))
                    showSnackbarMessage("Turned light ${if (!uiState.isOn) "on" else "off"}.")
                }
            },
            modifier = Modifier.padding(vertical = 32.dp)
        )

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
                        viewModel.updateLight(uiState.copy(brightness = brightness))
                        showSnackbarMessage("Adjusted light brightness.")
                    }
                }
            )
        }

        CheckboxOption(
            selected = uiState.isFavorite,
            optionText = stringResource(R.string.add_to_home_screen_for_quick_access),
            onClick = {
                coroutineScope.launch {
                    viewModel.updateLight(uiState.copy(isFavorite = !uiState.isFavorite))
                    showSnackbarMessage(
                        if (!uiState.isFavorite) {
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

        Button(onClick = { navigateToLightEdit(uiState.id) }) {
            Text(
                text = stringResource(R.string.edit_light_information),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}