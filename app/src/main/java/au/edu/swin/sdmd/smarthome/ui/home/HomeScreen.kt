package au.edu.swin.sdmd.smarthome.ui.home

import android.icu.text.SimpleDateFormat
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import co.yml.charts.axis.AxisData
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.Date


// The app's home screen.
@Composable
fun HomeScreen(
    navigateToSensor: (String) -> Unit,
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

//    Values for chart
//    val getTempData =  viewModel.listTemp
//    val steps = 5
//    val xAxisTempData = AxisData.Builder()
//        .axisStepSize(30.dp)
//        .steps(getTempData.size - 1)
//        .labelData { i -> i.toString() }
//        .labelAndAxisLinePadding(15.dp)
//        .build()
//    val yAxisTempData = AxisData.Builder()
//        .steps(steps)
//        .labelAndAxisLinePadding(30.dp)
//        .labelData { i ->
//            // Add yMin to get the negative axis values to the scale
//            val yMin = 0f
//            val yMax = getTempData.maxOf { it.y }
//            val yScale = (yMax - yMin) / steps
//            ((i * yScale) + yMin).formatToSinglePrecision()
//        }
//        .build()
//    val tempChartData = LineChartData(
//        linePlotData = LinePlotData(
//            lines = listOf(
//                Line(
//                    dataPoints = getTempData,
//                    LineStyle(lineType = LineType.SmoothCurve()),
//                    IntersectionPoint(),
//                    SelectionHighlightPoint(),
//                    ShadowUnderLine(),
//                    SelectionHighlightPopUp()
//                )
//            )
//        ),
//        xAxisData = xAxisTempData,
//        yAxisData = yAxisTempData,
//        gridLines = GridLines()
//    )


//    val getHumiData =  viewModel.listHumi
//    val xAxisHumiData = AxisData.Builder()
//        .axisStepSize(30.dp)
//        .steps(getHumiData.size - 1)
//        .labelData { i -> i.toString() }
//        .labelAndAxisLinePadding(15.dp)
//        .build()
//    val yAxisHumiData = AxisData.Builder()
//        .steps(steps)
//        .labelAndAxisLinePadding(30.dp)
//        .labelData { i ->
//            // Add yMin to get the negative axis values to the scale
//            val yMin = 0f
//            val yMax = getHumiData.maxOf { it.y }
//            val yScale = (yMax - yMin) / steps
//            ((i * yScale) + yMin).formatToSinglePrecision()
//        }
//        .build()
//    val humiChartData = LineChartData(
//        linePlotData = LinePlotData(
//            lines = listOf(
//                Line(
//                    dataPoints = getHumiData,
//                    LineStyle(lineType = LineType.SmoothCurve()),
//                    IntersectionPoint(),
//                    SelectionHighlightPoint(),
//                    ShadowUnderLine(),
//                    SelectionHighlightPopUp()
//                )
//            )
//        ),
//        xAxisData = xAxisHumiData,
//        yAxisData = yAxisHumiData,
//        gridLines = GridLines()
//    )


//    val getLightData =  viewModel.listLight
//    val xAxisLightData = AxisData.Builder()
//        .axisStepSize(30.dp)
//        .steps(getLightData.size - 1)
//        .labelData { i -> i.toString() }
//        .labelAndAxisLinePadding(15.dp)
//        .build()
//    val yAxisLightData = AxisData.Builder()
//        .steps(steps)
//        .labelAndAxisLinePadding(30.dp)
//        .labelData { i ->
//            // Add yMin to get the negative axis values to the scale
//            val yMin = 0f
//            val yMax = getLightData.maxOf { it.y }
//            val yScale = (yMax - yMin) / steps
//            ((i * yScale) + yMin).formatToSinglePrecision()
//        }
//        .build()
//    val lightChartData = LineChartData(
//        linePlotData = LinePlotData(
//            lines = listOf(
//                Line(
//                    dataPoints = getLightData,
//                    LineStyle(lineType = LineType.SmoothCurve()),
//                    IntersectionPoint(),
//                    SelectionHighlightPoint(),
//                    ShadowUnderLine(),
//                    SelectionHighlightPopUp()
//                )
//            )
//        ),
//        xAxisData = xAxisLightData,
//        yAxisData = yAxisLightData,
//        gridLines = GridLines()
//    )

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
                    metricMeasurementText = "${temperatureData.value}°C",
                    metricTimeString = "at ${formatter.format(temperatureData.time)}",
                    loading = temperatureData.time.compareTo(Date(0)) == 0,
                    modifier = Modifier.weight(1f).clickable { navigateToSensor("temp") }
                )

                MetricCard(
                    metricIconResource = R.drawable.humidity_24px,
                    metricMeasurementText = "${humidityData.value}%",
                    metricTimeString = "at ${formatter.format(humidityData.time)}",
                    loading = humidityData.time.compareTo(Date(0)) == 0,
                    modifier = Modifier.weight(1f).clickable { navigateToSensor("humi") }
                )

                MetricCard(
                    metricIconResource = R.drawable.light_24px,
                    metricMeasurementText = "${lightData.value} lx",
                    metricTimeString = "at ${formatter.format(lightData.time)}",
                    loading = lightData.time.compareTo(Date(0)) == 0,
                    modifier = Modifier.weight(1f).clickable { navigateToSensor("light") }
                )
            }
        }


//        item {
//            Column(
//                modifier = Modifier.fillMaxWidth().wrapContentHeight()
//            ) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .height(200.dp),
//                    contentAlignment = Alignment.TopCenter
//                ) {
//
//                    LineChart(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(300.dp),
//                        lineChartData = tempChartData
//                    )
//                }
//                Text(
//                    modifier = Modifier.fillMaxWidth().wrapContentHeight(),
//                    text = "Temperature change",
//                    style = MaterialTheme.typography.bodyMedium,
//                    textAlign = TextAlign.Center
//                )
//            }
//        }
//
//        item {
//            Column(
//                modifier = Modifier.fillMaxWidth().wrapContentHeight()
//            ) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .height(200.dp),
//                    contentAlignment = Alignment.TopCenter
//                ) {
//
//                    LineChart(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(300.dp),
//                        lineChartData = humiChartData
//                    )
//                }
//                Text(
//                    modifier = Modifier.fillMaxWidth().wrapContentHeight(),
//                    text = "Humidity change",
//                    style = MaterialTheme.typography.bodyMedium,
//                    textAlign = TextAlign.Center
//                )
//            }
//        }
//
//        item {
//            Column(
//                modifier = Modifier.fillMaxWidth().wrapContentHeight()
//            ) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .height(200.dp),
//                    contentAlignment = Alignment.TopCenter
//                ) {
//
//                    LineChart(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(300.dp),
//                        lineChartData = lightChartData
//                    )
//                }
//                Text(
//                    modifier = Modifier.fillMaxWidth().wrapContentHeight(),
//                    text = "Light change",
//                    style = MaterialTheme.typography.bodyMedium,
//                    textAlign = TextAlign.Center
//                )
//            }
//        }

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