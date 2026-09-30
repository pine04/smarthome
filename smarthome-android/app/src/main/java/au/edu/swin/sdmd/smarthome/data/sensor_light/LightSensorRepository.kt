package au.edu.swin.sdmd.smarthome.data.sensor_light

import kotlinx.coroutines.flow.Flow

interface LightSensorRepository {
    fun getLightData() : Flow<LightSensorData>
    fun getLightSensorStatus() : Flow<Boolean>
    fun turnOnSensor()
    fun turnOffSensor()
}