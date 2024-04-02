package au.edu.swin.sdmd.smarthome.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import au.edu.swin.sdmd.smarthome.data.Room

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
            label = {
                Text(
                    text = "Device name",
                    style = MaterialTheme.typography.labelSmall
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(
            expanded = roomDropdownExpanded,
            onExpandedChange = { roomDropdownExpanded = !roomDropdownExpanded },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            TextField(
                readOnly = true,
                value = room.value,
                onValueChange = { },
                label = {
                    Text(
                        text = "Room",
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = roomDropdownExpanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
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
            Text(
                text = "Edit",
                style = MaterialTheme.typography.labelLarge
            )
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        OutlinedButton(
            onClick = {
                isAlertDialogOpen = true
            }
        ) {
            Text(
                text = "Remove from home",
                style = MaterialTheme.typography.labelLarge
            )
        }

        if (isAlertDialogOpen) {
            AlertDialog(
                icon = { Icon(imageVector = Icons.Filled.Warning, contentDescription = "") },
                title = { Text(text = "Warning", style = MaterialTheme.typography.titleMedium) },
                text = {
                    Text(
                        text = "You are trying to remove $originalName from your home. This action is irreversible. Do you want to continue?",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                onDismissRequest = { isAlertDialogOpen = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            isAlertDialogOpen = false
                            onConfirmDelete()
                        }
                    ) {
                        Text(text = "Yes", style = MaterialTheme.typography.bodyMedium)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { isAlertDialogOpen = false }) {
                        Text(text = "No", style = MaterialTheme.typography.bodyMedium)
                    }
                },
            )
        }
    }
}