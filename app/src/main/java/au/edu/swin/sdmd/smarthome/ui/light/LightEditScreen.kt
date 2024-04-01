package au.edu.swin.sdmd.smarthome.ui.light

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import au.edu.swin.sdmd.smarthome.LightEditDestination
import au.edu.swin.sdmd.smarthome.MainApplication
import au.edu.swin.sdmd.smarthome.data.Room
import au.edu.swin.sdmd.smarthome.data.light.Light
import au.edu.swin.sdmd.smarthome.data.light.LightRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun LightEditScreen(
    navigateBack: () -> Unit,
    navigateToLights: () -> Unit,
    viewModel: LightEditViewModel = viewModel(factory = LightEditViewModel.Factory)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceEditForm(
    originalName: String,
    deviceName: String,
    onDeviceNameChange: (String) -> Unit,
    room: Room,
    onRoomChange: (Room) -> Unit,
    isValid: Boolean,
    onConfirmEdit: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    var roomDropdownExpanded by remember { mutableStateOf(false) }
    var isAlertDialogOpen by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        TextField(
            value = deviceName,
            onValueChange = onDeviceNameChange,
            label = { Text(text = "Device name") }
        )

        ExposedDropdownMenuBox(
            expanded = roomDropdownExpanded,
            onExpandedChange = { roomDropdownExpanded = !roomDropdownExpanded }) {
            TextField(
                readOnly = true,
                value = room.value,
                onValueChange = { },
                label = { Text(text = "Room") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = roomDropdownExpanded)
                },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = roomDropdownExpanded,
                onDismissRequest = { roomDropdownExpanded = false }
            ) {
                Room.entries.map { option ->
                    DropdownMenuItem(
                        text = { Text(text = option.value) },
                        onClick = { 
                            onRoomChange(option)
                            roomDropdownExpanded = false 
                        }
                    )
                }
            }
        }

        Button(
            enabled = isValid,
            onClick = onConfirmEdit
        ) {
            Text(text = "Edit")
        }

        HorizontalDivider()

        OutlinedButton(
            onClick = {
                isAlertDialogOpen = true
            }
        ) {
            Text(text = "Remove from home")
        }

        if (isAlertDialogOpen) {
            AlertDialog(
                icon = { Icon(imageVector = Icons.Filled.Warning, contentDescription = "") },
                title = { Text("Warning") },
                text = { Text("You are trying to remove $originalName from your home. This action is irreversible. Do you want to continue?") },
                onDismissRequest = { isAlertDialogOpen = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            isAlertDialogOpen = false
                            onConfirmDelete()
                        }
                    ) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { isAlertDialogOpen = false }) {
                        Text("No")
                    }
                },
            )
        }
    }
}

class LightEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val lightRepository: LightRepository
) : ViewModel() {

    var lightEditUiState by mutableStateOf(LightEditUiState())
        private set

    var originalName by mutableStateOf("")
        private set

    private lateinit var originalLight: Light

    private val lightId: Int = checkNotNull(savedStateHandle[LightEditDestination.lightIdArg])

    init {
        viewModelScope.launch {
            originalLight = lightRepository.getLightById(lightId).filterNotNull().first()
            lightEditUiState = originalLight.toLightEditUiState()
            originalName = originalLight.name
        }
    }

    fun updateLightDetails(lightDetails: LightDetails) {
        lightEditUiState = LightEditUiState(lightDetails, isValid(lightDetails))
    }

    suspend fun updateLight() {
        lightRepository.updateLight(lightEditUiState.lightDetails.toLight(originalLight))
    }

    suspend fun deleteLight() {
        lightRepository.deleteLight(originalLight)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val container = (this[APPLICATION_KEY] as MainApplication).container
                val lightRepository = container.lightRepository
                LightEditViewModel(this.createSavedStateHandle(), lightRepository)
            }
        }
    }

    private fun isValid(lightDetails: LightDetails): Boolean {
        return lightDetails.name.isNotBlank()
    }
}

data class LightEditUiState(
    val lightDetails: LightDetails = LightDetails(),
    val isValid: Boolean = false
)

data class LightDetails(
    val name: String = "",
    val location: Room = Room.LIVING_ROOM
)

fun Light.toLightEditUiState(): LightEditUiState = LightEditUiState(
    lightDetails = LightDetails(name = name, location = Room.entries.find { it.value == location } ?: Room.LIVING_ROOM),
    isValid = true
)

fun LightDetails.toLight(originalLight: Light): Light = originalLight.copy(
    name = name,
    location = location.value
)