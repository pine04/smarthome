package au.edu.swin.sdmd.smarthome.ui.devices

import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import au.edu.swin.sdmd.smarthome.MainApplication
import au.edu.swin.sdmd.smarthome.R
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditioner
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditionerRepository
import au.edu.swin.sdmd.smarthome.data.light.Light
import au.edu.swin.sdmd.smarthome.data.light.LightRepository
import au.edu.swin.sdmd.smarthome.ui.theme.AppTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class DevicesScreenViewModel(
    private val airConditionerRepository: AirConditionerRepository,
    private val lightRepository: LightRepository
) : ViewModel() {

    val uiState: StateFlow<DevicesScreenUiState> = combine(
        lightRepository.getLightCount(),
        lightRepository.getActiveLightCount(),
        airConditionerRepository.getAirConditionerCount(),
        airConditionerRepository.getActiveAirConditionerCount()
    ) { lights, activeLights, airConditioners, activeAirConditioners ->
        DevicesScreenUiState(lights, activeLights, airConditioners, activeAirConditioners)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = DevicesScreenUiState()
    )

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val container = (this[APPLICATION_KEY] as MainApplication).container
                val airConditionerRepository = container.airConditionerRepository
                val lightRepository = container.lightRepository
                DevicesScreenViewModel(airConditionerRepository, lightRepository)
            }
        }
    }
}

data class DevicesScreenUiState(
    val allLights: Int = 0,
    val allActiveLights: Int = 0,
    val allAirConditioners: Int = 0,
    val allActiveAirConditioners: Int = 0
)

@Composable
fun DevicesScreen(
    navigateToLights: () -> Unit,
    navigateToAirConditioners: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DevicesScreenViewModel = viewModel(factory = DevicesScreenViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(16.dp)
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
            .clickable { navigateToCategory() }
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
                Text(stringResource(id = nameResId))
                Text("$activeDeviceCount/$totalDeviceCount active")
            }

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.chevron_right_24px),
                contentDescription = "",
                modifier = Modifier
                    .size(24.dp)
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