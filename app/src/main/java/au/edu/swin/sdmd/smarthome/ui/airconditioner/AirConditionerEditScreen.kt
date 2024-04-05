package au.edu.swin.sdmd.smarthome.ui.airconditioner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.swin.sdmd.smarthome.ui.SmartHomeViewModelFactory
import au.edu.swin.sdmd.smarthome.ui.components.DeviceEditForm
import kotlinx.coroutines.launch

// Screen that allows the user to edit an air conditioner's information.
@Composable
fun AirConditionerEditScreen(
    navigateBackAfterEdit: () -> Unit,
    navigateBackAfterDelete: () -> Unit,
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
                navigateBackAfterEdit()
            }
        },
        onConfirmDelete = {
            coroutineScope.launch {
                viewModel.delete()
                navigateBackAfterDelete()
            }
        }
    )
}