package au.edu.swin.sdmd.smarthome.ui.devices

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
import androidx.compose.material3.MaterialTheme
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
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.swin.sdmd.smarthome.ui.SmartHomeViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceAddScreen(
    navigateBack: () -> Unit,
    viewModel: DeviceAddViewModel = viewModel(factory = SmartHomeViewModelFactory)
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
            onExpandedChange = { deviceDropdownExpanded = !deviceDropdownExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                readOnly = true,
                value = details.deviceType,
                onValueChange = { },
                label = { Text(text = "Device type") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = deviceDropdownExpanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
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
            onExpandedChange = { roomDropdownExpanded = !roomDropdownExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                readOnly = true,
                value = details.room,
                onValueChange = { },
                label = { Text(text = "Room") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = roomDropdownExpanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
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
            modifier = Modifier.fillMaxWidth()
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
            Text(
                text = "Add device",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}