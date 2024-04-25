package au.edu.swin.sdmd.smarthome.ui.home

import android.icu.text.SimpleDateFormat
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.swin.sdmd.smarthome.R
import au.edu.swin.sdmd.smarthome.ui.SmartHomeViewModelFactory
import au.edu.swin.sdmd.smarthome.ui.components.AirConditionerItem
import au.edu.swin.sdmd.smarthome.ui.components.LightItem
import au.edu.swin.sdmd.smarthome.ui.theme.AppTheme
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.Date


// The app's home screen.
@Composable
fun HomeScreen(
    navigateToLightControls: (Int) -> Unit,
    navigateToAirConditionerControls: (Int) -> Unit,
    navigateToLightsScreen: () -> Unit,
    navigateToAirConditionersScreen: () -> Unit,
    viewModel: HomeViewModel = viewModel(factory = SmartHomeViewModelFactory)
) {
    val uiState = viewModel.uiState.collectAsState().value
    val temperatureData = viewModel.temperature.collectAsState().value
    val humidityData = viewModel.humidity.collectAsState().value
    val lightData = viewModel.light.collectAsState().value
    val username = uiState.username
    val favoriteLights = uiState.favoriteLights
    val favoriteAirConditioners = uiState.favoriteAirConditioners

    val scope = rememberCoroutineScope()
    val formatter = SimpleDateFormat("hh:mm:ss", Locale.getDefault())

    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.welcome_home, username),
                style = MaterialTheme.typography.displayLarge
            )
        }

        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricCard(
                    metricIconResource = R.drawable.temperature_24px,
                    metricMeasurementText = "${temperatureData.temperature}°C",
                    metricTimeString = "at ${formatter.format(temperatureData.time)}",
                    loading = temperatureData.time.compareTo(Date(0)) == 0,
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    metricIconResource = R.drawable.humidity_24px,
                    metricMeasurementText = "${humidityData.humidity}%",
                    metricTimeString = "at ${formatter.format(humidityData.time)}",
                    loading = humidityData.time.compareTo(Date(0)) == 0,
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    metricIconResource = R.drawable.light_24px,
                    metricMeasurementText = "${lightData.light} lx",
                    metricTimeString = "at ${formatter.format(lightData.time)}",
                    loading = lightData.time.compareTo(Date(0)) == 0,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Text(
                text = stringResource(id = R.string.lights),
                style = MaterialTheme.typography.headlineMedium
            )
        }

        if (favoriteLights.isEmpty()) {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.you_have_not_pinned_any_lights_to_the_home_screen),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Button(onClick = navigateToLightsScreen) {
                        Text(stringResource(id = R.string.add_a_device))
                    }
                }
            }
        }

        items(favoriteLights) { light ->
            LightItem(
                light = light,
                navigateToLightControls = navigateToLightControls,
                toggleLight = { state ->
                    scope.launch {
                        viewModel.updateLight(light.copy(isOn = state))
                    }
                }
            )
        }

        item {
            Text(
                text = stringResource(id = R.string.air_conditioners),
                style = MaterialTheme.typography.headlineMedium
            )
        }

        if (favoriteAirConditioners.isEmpty()) {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.you_have_not_pinned_any_air_conditioners_to_the_home_screen),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Button(onClick = navigateToAirConditionersScreen) {
                        Text(stringResource(id = R.string.add_a_device))
                    }
                }
            }
        }

        items(favoriteAirConditioners) { airConditioner ->
            AirConditionerItem(
                airConditioner = airConditioner,
                navigateToAirConditionerControls = navigateToAirConditionerControls,
                toggleAirConditioner = { state ->
                    scope.launch {
                        viewModel.updateAirConditioner(airConditioner.copy(isOn = state))
                    }
                }
            )
        }
    }
}

@Composable
fun MetricCard(
    @DrawableRes metricIconResource: Int,
    metricMeasurementText: String,
    metricTimeString: String,
    modifier: Modifier = Modifier,
    loading: Boolean = false
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                painter = painterResource(id = metricIconResource),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )

            Text(
                text = if (loading) "N/A" else metricMeasurementText,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                text = if (loading) "N/A" else metricTimeString,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview
@Composable
fun MetricCardPreview() {
    AppTheme {
        MetricCard(
            metricIconResource = R.drawable.temperature_24px,
            metricMeasurementText = "25°C",
            metricTimeString = "6:30:00"
        )
    }
}