package au.edu.swin.sdmd.smarthome.ui.rooms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.swin.sdmd.smarthome.R
import au.edu.swin.sdmd.smarthome.ui.SmartHomeViewModelFactory
import au.edu.swin.sdmd.smarthome.ui.components.AirConditionerItem
import au.edu.swin.sdmd.smarthome.ui.components.LightItem
import kotlinx.coroutines.launch

// Screen that displays a list of lights and air conditioners of a specific room.
@Composable
fun SingleRoomScreen(
    navigateToLightControls: (Int) -> Unit,
    navigateToAirConditionerControls: (Int) -> Unit,
    viewModel: SingleRoomViewModel = viewModel(factory = SmartHomeViewModelFactory)
) {
    val uiState = viewModel.uiState.collectAsState()
    val lights = uiState.value.lights
    val airConditioners = uiState.value.airConditioners

    val scope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = stringResource(id = R.string.lights),
                style = MaterialTheme.typography.headlineMedium
            )
        }

        if (lights.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.you_do_not_have_any_lights_in_this_room),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }
        }

        items(lights) { light ->
            LightItem(
                light = light,
                navigateToLightControls = navigateToLightControls,
                toggleLight = { state ->
                    scope.launch {
                        viewModel.updateLight(light.copy(isOn = state))
                    }
                }
            )
        }

        item {
            Text(
                text = stringResource(id = R.string.air_conditioners),
                style = MaterialTheme.typography.headlineMedium
            )
        }

        if (airConditioners.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.you_do_not_have_any_air_conditioners_in_this_room),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }
        }

        items(airConditioners) { airConditioner ->
            AirConditionerItem(
                airConditioner = airConditioner,
                navigateToAirConditionerControls = navigateToAirConditionerControls,
                toggleAirConditioner = { state ->
                    scope.launch {
                        viewModel.updateAirConditioner(airConditioner.copy(isOn = state))
                    }
                }
            )
        }
    }
}

