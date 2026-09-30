package au.edu.swin.sdmd.smarthome.ui.sensors

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.swin.sdmd.smarthome.R
import au.edu.swin.sdmd.smarthome.ui.SmartHomeViewModelFactory
import au.edu.swin.sdmd.smarthome.ui.home.MetricCard
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
import java.util.Date
import java.util.Locale

// Screen that displays a list of lights and air conditioners of a specific room.
@Composable
fun SingleSensorScreen(
    viewModel: SingleSensorViewModel = viewModel(factory = SmartHomeViewModelFactory)
) {

    val data = viewModel.dataFlow.collectAsState().value

    val formatter = SimpleDateFormat("hh:mm:ss", Locale.getDefault())
    var name = "Temperature"
    var unit = "°C"
    var icon = R.drawable.temperature_24px
    val isOn = viewModel.isOn.collectAsState().value
    val turnOnSensor = viewModel.turnOnSensor
    val turnOffSensor = viewModel.turnOffSensor
    when(viewModel.sensorType) {
        "temp" -> {
            name = "Temperature"
            unit = "°C"
            icon = R.drawable.temperature_24px
        }
        "humi" -> {
            name = "Humidity"
            unit = "%"
            icon = R.drawable.humidity_24px
        }
        "light" -> {
            name = "Light"
            unit = "lx"
            icon = R.drawable.light_24px
        }
    }

//    Values for chart
    val listData =  viewModel.listData
    val steps = 5
    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .steps(listData.size - 1)
        .labelData { i -> i.toString() }
        .labelAndAxisLinePadding(15.dp)
        .build()
    val yAxisData = AxisData.Builder()
        .steps(steps)
        .labelAndAxisLinePadding(30.dp)
        .labelData { i ->
            // Add yMin to get the negative axis values to the scale
            val yMin = 0f
            val yMax = listData.maxOf { it.y }
            val yScale = (yMax - yMin) / steps
            ((i * yScale) + yMin).formatToSinglePrecision()
        }
        .build()
    val chartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = listData,
                    LineStyle(lineType = LineType.SmoothCurve()),
                    IntersectionPoint(),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(),
                    SelectionHighlightPopUp()
                )
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines()
    )
    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            MetricCard(
                metricIconResource = icon,
                metricMeasurementText = "${data.value} $unit",
                metricTimeString = "at ${formatter.format(data.time)}",
                loading = data.time.compareTo(Date(0)) == 0,
                isOn = isOn,
                setIsOn = { on ->
                    if (on) {
                        turnOnSensor()
                    } else {
                        turnOffSensor()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        }


        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(200.dp),
                    contentAlignment = Alignment.TopCenter
                ) {

                    LineChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        lineChartData = chartData
                    )
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    text = "$name change",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

