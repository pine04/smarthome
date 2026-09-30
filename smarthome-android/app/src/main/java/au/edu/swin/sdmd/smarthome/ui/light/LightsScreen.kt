package au.edu.swin.sdmd.smarthome.ui.light

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
import au.edu.swin.sdmd.smarthome.ui.components.LightItem
import kotlinx.coroutines.launch

// Screen that lists all available lights in the home.
@Composable
fun LightsScreen(
    navigateToLightControls: (Int) -> Unit,
    viewModel: LightsViewModel = viewModel(factory = SmartHomeViewModelFactory)
) {
    val lights = viewModel.lightsUiState.collectAsState().value.lights
    val scope = rememberCoroutineScope()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        if (lights.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.you_do_not_have_any_lights_in_your_home),
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
    }
}

