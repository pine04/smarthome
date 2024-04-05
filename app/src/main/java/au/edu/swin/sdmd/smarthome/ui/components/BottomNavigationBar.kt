package au.edu.swin.sdmd.smarthome.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import au.edu.swin.sdmd.smarthome.DevicesDestination
import au.edu.swin.sdmd.smarthome.HomeDestination
import au.edu.swin.sdmd.smarthome.NavigationDestination
import au.edu.swin.sdmd.smarthome.R
import au.edu.swin.sdmd.smarthome.RoomsDestination
import au.edu.swin.sdmd.smarthome.SettingsDestination
import au.edu.swin.sdmd.smarthome.ui.theme.AppTheme

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

val destinationsWithBottomNav = arrayOf(
    HomeDestination,
    DevicesDestination,
    RoomsDestination,
    SettingsDestination
)

// The bottom navigation bar component.
@Composable
fun BottomNavigationBar(
    currentDestination: NavigationDestination,
    navigateToDestination: (String) -> Unit
) {
    if (!destinationsWithBottomNav.contains(currentDestination)) {
        return
    }

    NavigationBar {
        bottomNavigationDestinations.map { destination ->
            NavigationBarItem(
                selected = destination.destination == currentDestination,
                onClick = { navigateToDestination(destination.destination.route) },
                label = {
                    Text(
                        text = stringResource(id = destination.destination.titleResId),
                        style = MaterialTheme.typography.labelSmall
                    )
                },
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

@Preview
@Composable
fun BottomNavigationBarPreview() {
    AppTheme(darkModeOption = "auto") {
        BottomNavigationBar(currentDestination = HomeDestination, navigateToDestination = { })
    }
}