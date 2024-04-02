package au.edu.swin.sdmd.smarthome.ui.light

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import au.edu.swin.sdmd.smarthome.LightControlsDestination
import au.edu.swin.sdmd.smarthome.MainApplication
import au.edu.swin.sdmd.smarthome.R
import au.edu.swin.sdmd.smarthome.data.light.Light
import au.edu.swin.sdmd.smarthome.data.light.LightRepository
import au.edu.swin.sdmd.smarthome.ui.SmartHomeViewModelFactory
import au.edu.swin.sdmd.smarthome.ui.components.CheckboxOption
import au.edu.swin.sdmd.smarthome.ui.components.OnOffButton
import au.edu.swin.sdmd.smarthome.ui.devices.DevicesScreenUiState
import au.edu.swin.sdmd.smarthome.ui.theme.AppTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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

        Text(
            text = "Brightness",
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

        CheckboxOption(
            selected = uiState.isFavorite,
            optionText = "Add to home screen for quick access",
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
                text = "Edit light information",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}