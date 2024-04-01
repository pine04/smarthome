package au.edu.swin.sdmd.smarthome.ui.light

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import au.edu.swin.sdmd.smarthome.ui.devices.DevicesScreenUiState
import au.edu.swin.sdmd.smarthome.ui.theme.AppTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LightControlsViewModel(
    savedStateHandle: SavedStateHandle,
    private val lightRepository: LightRepository
) : ViewModel() {
    private val lightId: Int = checkNotNull(savedStateHandle[LightControlsDestination.lightIdArg])

    val uiState: StateFlow<Light> = lightRepository.getLightById(lightId).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = Light(name = "My Light", location = "Living room")
    )

    suspend fun updateLight(light: Light) {
        lightRepository.updateLight(light)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val container = (this[APPLICATION_KEY] as MainApplication).container
                val lightRepository = container.lightRepository
                LightControlsViewModel(this.createSavedStateHandle(), lightRepository)
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LightControlsScreen(
    navigateToLightEdit: (Int) -> Unit,
    viewModel: LightControlsViewModel = viewModel(factory = LightControlsViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var brightness by remember { mutableFloatStateOf(uiState.brightness) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(uiState.name)
        Text(uiState.location)
        Text(uiState.isFavorite.toString())

        Text(uiState.toString())

        OnOffButton(
            isOn = uiState.isOn,
            setIsOn = {
                coroutineScope.launch {
                    viewModel.updateLight(uiState.copy(isOn = !uiState.isOn))
                }
            }
        )

        Slider(
            value = brightness,
            valueRange = 0f..1f,
            onValueChange = { brightness = it },
            onValueChangeFinished = {
                coroutineScope.launch {
                    viewModel.updateLight(uiState.copy(brightness = brightness))
                }
            }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = uiState.isFavorite,
                onCheckedChange = {
                    coroutineScope.launch {
                        viewModel.updateLight(uiState.copy(isFavorite = !uiState.isFavorite))
                    }
                }
            )
            Text("Add to home screen for quick access")
        }

        Button(onClick = { navigateToLightEdit(uiState.id) }) {
            Text(text = "Edit light information")
        }
    }
}

@Composable
fun OnOffButton(
    isOn: Boolean,
    setIsOn: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonColors = if (isOn) {
        ButtonDefaults.buttonColors()
    } else {
        ButtonDefaults.filledTonalButtonColors()
    }

    Button(
        onClick = { setIsOn(!isOn) },
        modifier = modifier.size(192.dp),
        colors = buttonColors
    ) {
        Icon(
            painter = painterResource(id = R.drawable.mode_off_on_24px),
            contentDescription = "",
            modifier = Modifier.size(96.dp)
        )
    }
}

@Preview
@Composable
fun OnOffButtonPreview() {
    AppTheme {
        OnOffButton(isOn = true, setIsOn = { })
    }
}