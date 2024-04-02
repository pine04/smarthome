package au.edu.swin.sdmd.smarthome.ui.light

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.swin.sdmd.smarthome.ui.SmartHomeViewModelFactory
import au.edu.swin.sdmd.smarthome.ui.components.LightItem
import kotlinx.coroutines.launch

@Composable
fun LightsScreen(
    navigateToLightControls: (Int) -> Unit,
    viewModel: LightsViewModel = viewModel(factory = SmartHomeViewModelFactory)
) {
    val lightsUiState by viewModel.lightsUiState.collectAsState()
    val scope = rememberCoroutineScope()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(lightsUiState.lights) { light ->
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

