package au.edu.swin.sdmd.smarthome.data.sensor_humidity

import java.util.Date

data class HumiditySensorData(
    val time: Date = Date(0),
    val humidity: Int = 0
)