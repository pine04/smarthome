package au.edu.swin.sdmd.smarthome.ui.airconditioner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.swin.sdmd.smarthome.ui.SmartHomeViewModelFactory
import au.edu.swin.sdmd.smarthome.ui.components.DeviceEditForm
import kotlinx.coroutines.launch

@Composable
fun AirConditionerEditScreen(
    navigateBack: () -> Unit,
    navigateToAirConditioners: () -> Unit,
    viewModel: AirConditionerEditViewModel = viewModel(factory = SmartHomeViewModelFactory)
) {
    val coroutineScope = rememberCoroutineScope()
    val uiState = viewModel.uiState
    val originalName = viewModel.originalName

    DeviceEditForm(
        originalName = originalName,
        deviceName = uiState.details.name,
        onDeviceNameChange = { name ->
            viewModel.updateAirConditionerDetails(uiState.details.copy(name = name))
        },
        room = uiState.details.location,
        onRoomChange = { room ->
            viewModel.updateAirConditionerDetails(uiState.details.copy(location = room))
        },
        isValid = viewModel.uiState.isValid,
        onConfirmEdit = {
            coroutineScope.launch {
                viewModel.update()
                navigateBack()
            }
        },
        onConfirmDelete = {
            coroutineScope.launch {
                viewModel.delete()
                navigateToAirConditioners()
            }
        }
    )
}