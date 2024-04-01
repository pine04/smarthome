package au.edu.swin.sdmd.smarthome.ui.devices

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import au.edu.swin.sdmd.smarthome.MainApplication
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditioner
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditionerRepository
import au.edu.swin.sdmd.smarthome.data.light.Light
import au.edu.swin.sdmd.smarthome.data.light.OfflineLightRepository
import kotlinx.coroutines.launch

class DeviceAddScreenViewModel(
    private val airConditionerRepository: AirConditionerRepository,
    private val lightRepository: OfflineLightRepository
) :
    ViewModel() {
    var uiState by mutableStateOf(DeviceAddScreenUiState())
        private set

    fun updateDeviceDetails(details: DeviceDetails) {
        uiState = DeviceAddScreenUiState(details, validateDetails(details))
    }

    private fun validateDetails(details: DeviceDetails): Boolean {
        return false
    }

    suspend fun addDevice() {
        if (uiState.deviceDetails.deviceType == "Light") {
            Log.d("DeviceAddScreen", uiState.deviceDetails.toLight().toString())
            lightRepository.insertLight(uiState.deviceDetails.toLight())
        } else if (uiState.deviceDetails.deviceType == "Air conditioner") {
            Log.d("DeviceAddScreen", uiState.deviceDetails.toAirConditioner().toString())
            airConditionerRepository.insertAirConditioner(uiState.deviceDetails.toAirConditioner())
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val container = (this[APPLICATION_KEY] as MainApplication).container
                val airConditionerRepository = container.airConditionerRepository
                val lightRepository = container.lightRepository
                DeviceAddScreenViewModel(airConditionerRepository, lightRepository)
            }
        }
    }
}

fun DeviceDetails.toLight(): Light = Light(
    name = deviceName,
    location = room,
    isFavorite = isFavorite
)

fun DeviceDetails.toAirConditioner(): AirConditioner = AirConditioner(
    name = deviceName,
    location = room,
    isFavorite = isFavorite
)

data class DeviceDetails(
    val deviceType: String = "",
    val room: String = "",
    val deviceName: String = "",
    val isFavorite: Boolean = false
)

data class DeviceAddScreenUiState(
    val deviceDetails: DeviceDetails = DeviceDetails(),
    val isEntryValid: Boolean = false
)

val deviceOptions = arrayOf("Light", "Air conditioner")
val roomOptions =
    arrayOf("Living Room", "Bedroom", "Bathroom", "Kitchen", "Hallway", "Garage", "Attic")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceAddScreen(
    navigateBack: () -> Unit,
    viewModel: DeviceAddScreenViewModel = viewModel(factory = DeviceAddScreenViewModel.Factory)
) {
    var deviceDropdownExpanded by remember {
        mutableStateOf(false)
    }
    var roomDropdownExpanded by remember {
        mutableStateOf(false)
    }
    val details = viewModel.uiState.deviceDetails
    val coroutineScope = rememberCoroutineScope()


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = deviceDropdownExpanded,
            onExpandedChange = { deviceDropdownExpanded = !deviceDropdownExpanded }) {
            TextField(
                readOnly = true,
                value = details.deviceType,
                onValueChange = { },
                label = { Text(text = "Device type") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = deviceDropdownExpanded)
                },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = deviceDropdownExpanded,
                onDismissRequest = { deviceDropdownExpanded = false }) {
                deviceOptions.map { option ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            viewModel.updateDeviceDetails(details.copy(deviceType = option))
                            deviceDropdownExpanded = false
                        }
                    )
                }
            }
        }

        ExposedDropdownMenuBox(
            expanded = roomDropdownExpanded,
            onExpandedChange = { roomDropdownExpanded = !roomDropdownExpanded }) {
            TextField(
                readOnly = true,
                value = details.room,
                onValueChange = { },
                label = { Text(text = "Room") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = roomDropdownExpanded)
                },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = roomDropdownExpanded,
                onDismissRequest = { roomDropdownExpanded = false }) {
                roomOptions.map { option ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            viewModel.updateDeviceDetails(details.copy(room = option))
                            roomDropdownExpanded = false
                        }
                    )
                }
            }
        }

        TextField(
            value = details.deviceName,
            onValueChange = { viewModel.updateDeviceDetails(details.copy(deviceName = it)) },
            label = { Text("Device name") },
            placeholder = { Text("Bedroom table light") }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = details.isFavorite,
                onCheckedChange = { viewModel.updateDeviceDetails(details.copy(isFavorite = it)) }
            )
            Text(text = "Add to home screen for quick access")
        }

        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.addDevice()
                    navigateBack()
                }
            }
        ) {
            Text("Add device")
        }
    }
}