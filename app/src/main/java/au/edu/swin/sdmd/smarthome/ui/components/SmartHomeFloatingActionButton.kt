package au.edu.swin.sdmd.smarthome.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import au.edu.swin.sdmd.smarthome.AirConditionersDestination
import au.edu.swin.sdmd.smarthome.DeviceAddDestination
import au.edu.swin.sdmd.smarthome.DevicesDestination
import au.edu.swin.sdmd.smarthome.LightsDestination
import au.edu.swin.sdmd.smarthome.NavigationDestination
import au.edu.swin.sdmd.smarthome.R
import au.edu.swin.sdmd.smarthome.SingleRoomDestination
import au.edu.swin.sdmd.smarthome.ui.theme.AppTheme

// A floating action button that takes the user to the device add screen.
@Composable
fun SmartHomeFloatingActionButton(
    currentDestination: NavigationDestination,
    navigateTo: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (currentDestination) {
        DevicesDestination, LightsDestination, AirConditionersDestination, SingleRoomDestination -> {
            ExtendedFloatingActionButton(
                onClick = { navigateTo(DeviceAddDestination.route) },
                icon = { Icon(imageVector = Icons.Filled.Add, contentDescription = null) },
                text = { Text(text = stringResource(id = R.string.fab), style = MaterialTheme.typography.labelLarge) },
                modifier = modifier.semantics {
                    contentDescription = "Add a new device"
                }
            )
        }
    }
}

@Preview
@Composable
fun FABPreview() {
    AppTheme {
        SmartHomeFloatingActionButton(currentDestination = DevicesDestination, navigateTo = { })
    }
}