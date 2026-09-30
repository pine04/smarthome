package au.edu.swin.sdmd.smarthome.data.sensor_temperature

import kotlinx.coroutines.flow.Flow

interface TemperatureSensorRepository {
    fun getTemperatureData() : Flow<TemperatureSensorData>
    fun getTemperatureSensorStatus() : Flow<Boolean>
    fun turnOnSensor()
    fun turnOffSensor()
}