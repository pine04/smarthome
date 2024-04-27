package au.edu.swin.sdmd.smarthome.data.sensor_light

import java.util.Date

data class LightSensorData(
    val time: Date = Date(0),
    val light: Int = 0
)