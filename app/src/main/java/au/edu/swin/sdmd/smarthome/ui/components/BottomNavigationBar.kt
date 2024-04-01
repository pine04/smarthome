package au.edu.swin.sdmd.smarthome.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import au.edu.swin.sdmd.smarthome.DevicesDestination
import au.edu.swin.sdmd.smarthome.HomeDestination
import au.edu.swin.sdmd.smarthome.NavigationDestination
import au.edu.swin.sdmd.smarthome.R
import au.edu.swin.sdmd.smarthome.RoomsDestination
import au.edu.swin.sdmd.smarthome.SettingsDestination

data class BottomNavigationDestination(
    val destination: NavigationDestination,
    @DrawableRes val icon: Int
)

private val bottomNavigationDestinations = arrayOf(
    BottomNavigationDestination(HomeDestination, R.drawable.home_24px),
    BottomNavigationDestination(DevicesDestination, R.drawable.light_bulb_24px),
    BottomNavigationDestination(RoomsDestination, R.drawable.room_24px),
    BottomNavigationDestination(SettingsDestination, R.drawable.settings_24px)
)

@Composable
fun BottomNavigationBar(
    currentDestination: NavigationDestination,
    navigateToDestination: (String) -> Unit
) {
    NavigationBar {
        bottomNavigationDestinations.map { destination ->
            NavigationBarItem(
                selected = destination.destination == currentDestination,
                onClick = { navigateToDestination(destination.destination.route) },
                label = { Text(text = stringResource(id = destination.destination.titleResId)) },
                icon = {
                    Icon(
                        painter = painterResource(id = destination.icon),
                        contentDescription = ""
                    )
                }
            )
        }
    }
}

