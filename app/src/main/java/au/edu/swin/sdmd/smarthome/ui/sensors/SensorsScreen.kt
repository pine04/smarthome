package au.edu.swin.sdmd.smarthome.ui.sensors

import android.icu.text.SimpleDateFormat
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.swin.sdmd.smarthome.R
import au.edu.swin.sdmd.smarthome.ui.SmartHomeViewModelFactory
import au.edu.swin.sdmd.smarthome.ui.home.MetricCard
import java.util.Date
import java.util.Locale

// Screen that displays a list of sensors that the user can go to.
@Composable
fun SensorsScreen(
    navigateToSensor: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SensorsViewModel = viewModel(factory = SmartHomeViewModelFactory)
) {
    val temperatureData = viewModel.temperature.collectAsState().value
    val humidityData = viewModel.humidity.collectAsState().value
    val lightData = viewModel.light.collectAsState().value
    val formatter = SimpleDateFormat("hh:mm:ss", Locale.getDefault())

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(16.dp)
    ) {
        item {
            MetricCard(
                metricIconResource = R.drawable.temperature_24px,
                metricMeasurementText = "${temperatureData.value}°C",
                metricTimeString = "at ${formatter.format(temperatureData.time)}",
                loading = temperatureData.time.compareTo(Date(0)) == 0,
                modifier = Modifier.fillMaxWidth().wrapContentHeight().clickable { navigateToSensor("temp") }
            )
        }

        item {
            MetricCard(
                metricIconResource = R.drawable.humidity_24px,
                metricMeasurementText = "${humidityData.value}%",
                metricTimeString = "at ${formatter.format(humidityData.time)}",
                loading = humidityData.time.compareTo(Date(0)) == 0,
                modifier = Modifier.fillMaxWidth().wrapContentHeight().clickable { navigateToSensor("humi") }
            )
        }

        item {
            MetricCard(
                metricIconResource = R.drawable.light_24px,
                metricMeasurementText = "${lightData.value} lx",
                metricTimeString = "at ${formatter.format(lightData.time)}",
                loading = lightData.time.compareTo(Date(0)) == 0,
                modifier = Modifier.fillMaxWidth().wrapContentHeight().clickable { navigateToSensor("light") }
            )
        }
    }
}

