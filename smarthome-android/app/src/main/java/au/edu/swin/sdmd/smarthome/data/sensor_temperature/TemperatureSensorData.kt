package au.edu.swin.sdmd.smarthome.data.sensor_temperature

import java.util.Date

data class TemperatureSensorData(
    val time: Date = Date(0),
    val temperature: Int = 0
)