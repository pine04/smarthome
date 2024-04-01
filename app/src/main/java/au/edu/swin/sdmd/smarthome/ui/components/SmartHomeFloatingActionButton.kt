package au.edu.swin.sdmd.smarthome.ui.components

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import au.edu.swin.sdmd.smarthome.AirConditionersDestination
import au.edu.swin.sdmd.smarthome.DeviceAddDestination
import au.edu.swin.sdmd.smarthome.DevicesDestination
import au.edu.swin.sdmd.smarthome.LightControlsDestination
import au.edu.swin.sdmd.smarthome.LightEditDestination
import au.edu.swin.sdmd.smarthome.LightsDestination
import au.edu.swin.sdmd.smarthome.NavigationDestination

@Composable
fun SmartHomeFloatingActionButton(
    currentDestination: NavigationDestination,
    navigateTo: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (currentDestination) {
        DevicesDestination, LightsDestination, AirConditionersDestination -> {
            FloatingActionButton(
                onClick = { navigateTo(DeviceAddDestination.route) },
                modifier = modifier
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "")
            }
        }
    }
}