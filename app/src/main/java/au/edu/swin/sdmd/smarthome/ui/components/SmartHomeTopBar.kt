package au.edu.swin.sdmd.smarthome.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import au.edu.swin.sdmd.smarthome.DevicesDestination
import au.edu.swin.sdmd.smarthome.HomeDestination
import au.edu.swin.sdmd.smarthome.LightsDestination
import au.edu.swin.sdmd.smarthome.NavigationDestination
import au.edu.swin.sdmd.smarthome.R
import au.edu.swin.sdmd.smarthome.RoomsDestination
import au.edu.swin.sdmd.smarthome.SettingsDestination
import au.edu.swin.sdmd.smarthome.ui.theme.AppTheme

private val destinationsWithOutBackButton = arrayOf(
    HomeDestination,
    DevicesDestination,
    RoomsDestination,
    SettingsDestination
)

// A top bar that displays the name of the current screen and the option to go back if applicable.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartHomeTopBar(
    currentDestination: NavigationDestination,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(currentDestination.titleResId),
                style = MaterialTheme.typography.displayMedium
            )
        },
        navigationIcon = {
            if (!destinationsWithOutBackButton.contains(currentDestination)) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
fun SmartHomeTopBarNoBackButtonPreview() {
    AppTheme {
        SmartHomeTopBar(currentDestination = HomeDestination, navigateUp = { })
    }
}

@Preview
@Composable
fun SmartHomeTopBarWithBackButtonPreview() {
    AppTheme {
        SmartHomeTopBar(currentDestination = LightsDestination, navigateUp = { })
    }
}