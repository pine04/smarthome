package au.edu.swin.sdmd.smarthome.ui.light

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.swin.sdmd.smarthome.ui.SmartHomeViewModelFactory
import au.edu.swin.sdmd.smarthome.ui.components.DeviceEditForm
import kotlinx.coroutines.launch

@Composable
fun LightEditScreen(
    navigateBack: () -> Unit,
    navigateToLights: () -> Unit,
    viewModel: LightEditViewModel = viewModel(factory = SmartHomeViewModelFactory)
) {
    val coroutineScope = rememberCoroutineScope()
    val lightEditUiState = viewModel.lightEditUiState
    val originalName = viewModel.originalName
    
    DeviceEditForm(
        originalName = originalName,
        deviceName = lightEditUiState.lightDetails.name, 
        onDeviceNameChange = { name -> 
            viewModel.updateLightDetails(lightEditUiState.lightDetails.copy(name = name)) 
        },
        room = lightEditUiState.lightDetails.location,
        onRoomChange = { room ->
            viewModel.updateLightDetails(lightEditUiState.lightDetails.copy(location = room))
        },
        isValid = viewModel.lightEditUiState.isValid,
        onConfirmEdit = {
            coroutineScope.launch {
                viewModel.updateLight()
                navigateBack()
            }
        },
        onConfirmDelete = {
            coroutineScope.launch {
                viewModel.deleteLight()
                navigateToLights()
            }
        }
    )
}