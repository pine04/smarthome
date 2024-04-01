package au.edu.swin.sdmd.smarthome.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import au.edu.swin.sdmd.smarthome.NavigationDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartHomeTopBar(
    currentDestination: NavigationDestination,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(text = stringResource(currentDestination.titleResId)) },
        modifier = modifier
    )
}