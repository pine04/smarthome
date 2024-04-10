package au.edu.swin.sdmd.smarthome.ui.devices

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.swin.sdmd.smarthome.R
import au.edu.swin.sdmd.smarthome.ui.SmartHomeViewModelFactory
import au.edu.swin.sdmd.smarthome.ui.theme.AppTheme

// Screen where the user can see the device categories and how many devices are active for each category.
@Composable
fun DevicesScreen(
    navigateToLights: () -> Unit,
    navigateToAirConditioners: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DevicesViewModel = viewModel(factory = SmartHomeViewModelFactory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        DeviceCategory(
            iconResId = R.drawable.light_bulb_24px,
            nameResId = R.string.lights,
            activeDeviceCount = uiState.allActiveLights,
            totalDeviceCount = uiState.allLights,
            navigateToCategory = navigateToLights
        )

        DeviceCategory(
            iconResId = R.drawable.air_conditioner_24px,
            nameResId = R.string.air_conditioners,
            activeDeviceCount = uiState.allActiveAirConditioners,
            totalDeviceCount = uiState.allAirConditioners,
            navigateToCategory = navigateToAirConditioners
        )
    }
}

@Composable
fun DeviceCategory(
    @DrawableRes iconResId: Int,
    @StringRes nameResId: Int,
    activeDeviceCount: Int,
    totalDeviceCount: Int,
    navigateToCategory: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable(onClickLabel = stringResource(R.string.go_to_list)) { navigateToCategory() }
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = "",
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 16.dp)
            )

            Column {
                Text(
                    text = stringResource(id = nameResId),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = stringResource(R.string.active, activeDeviceCount, totalDeviceCount),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.chevron_right_24px),
                contentDescription = "",
                modifier = Modifier
                    .size(32.dp)
            )
        }
    }
}

@Preview
@Composable
fun DeviceCategoryPreview() {
    AppTheme {
        DeviceCategory(
            iconResId = R.drawable.light_bulb_24px,
            nameResId = R.string.lights,
            activeDeviceCount = 5,
            totalDeviceCount = 10,
            navigateToCategory = { /*TODO*/ }
        )
    }
}