package au.edu.swin.sdmd.smarthome.ui.light

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.swin.sdmd.smarthome.R
import au.edu.swin.sdmd.smarthome.data.light_trigger.LightAction
import au.edu.swin.sdmd.smarthome.ui.SmartHomeViewModelFactory
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LightTriggerEditScreen(
    navigateBack: () -> Unit,
    viewModel: LightTriggerEditViewModel = viewModel(factory = SmartHomeViewModelFactory)
) {
    val triggerDetails = viewModel.uiState.triggerDetails
    val coroutineScope = rememberCoroutineScope()

    var actionDropdownExpanded by remember {
        mutableStateOf(false)
    }

    var pickedTime by remember(triggerDetails.triggerTime) {
        mutableStateOf(triggerDetails.triggerTime)
    }
    val timePickerState = rememberMaterialDialogState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = actionDropdownExpanded,
            onExpandedChange = { actionDropdownExpanded = !actionDropdownExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                readOnly = true,
                value = stringResource(id = lightActionToStringResource(triggerDetails.triggerAction)),
                onValueChange = { },
                label = { Text(text = stringResource(R.string.action)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.action_24px),
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = actionDropdownExpanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = actionDropdownExpanded,
                onDismissRequest = { actionDropdownExpanded = false },
                modifier = Modifier
            ) {
                val selectLabel = stringResource(R.string.select)

                LightAction.entries.map { option ->
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = lightActionToStringResource(option))) },
                        onClick = {
                            viewModel.updateLightTriggerDetails(triggerDetails.copy(triggerAction = option))
                            actionDropdownExpanded = false
                        },
                        modifier = Modifier.semantics {
                            onClick(label = selectLabel, action = null)
                        }
                    )
                }
            }
        }

        val defaultTextFieldColors = TextFieldDefaults.colors()

        TextField(
            value = DateTimeFormatter
                .ofPattern("hh:mm a")
                .format(pickedTime),
            onValueChange = { },
            enabled = false,
            label = { Text(text = stringResource(R.string.trigger_time)) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.schedule_24px),
                    contentDescription = null
                )
            },
            colors = defaultTextFieldColors.copy(
                disabledTextColor = defaultTextFieldColors.unfocusedTextColor,
                disabledLabelColor = defaultTextFieldColors.unfocusedLabelColor,
                disabledLeadingIconColor = defaultTextFieldColors.unfocusedLeadingIconColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { timePickerState.show() }
        )

        MaterialDialog(
            dialogState = timePickerState,
            buttons = {
                positiveButton(text = stringResource(id = R.string.set)) {
                    viewModel.updateLightTriggerDetails(triggerDetails.copy(triggerTime = pickedTime))
                }
                negativeButton(text = stringResource(id = R.string.cancel))
            }
        ) {
            timepicker(
                initialTime = pickedTime,
                title = stringResource(R.string.set_a_time_for_the_trigger)
            ) {
                pickedTime = it
            }
        }

        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.updateTrigger()
                    navigateBack()
                }
            }
        ) {
            Text(
                text = stringResource(id = R.string.update_trigger)
            )
        }

        OutlinedButton(
            onClick = {
                coroutineScope.launch {
                    viewModel.deleteTrigger()
                    navigateBack()
                }
            }
        ) {
            Text(
                text = stringResource(R.string.delete_trigger),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

private fun lightActionToStringResource(lightAction: LightAction): Int =
    when (lightAction) {
        LightAction.TURN_ON -> R.string.action_turn_on
        LightAction.TURN_OFF -> R.string.action_turn_off
    }