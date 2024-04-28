package au.edu.swin.sdmd.smarthome.data

import java.util.Date

data class SensorData(
    val time: Date = Date(0),
    val value: Int = 0
)