package au.edu.swin.sdmd.smarthome.data.sensor_temperature

import kotlinx.coroutines.flow.Flow

interface TemperatureRepository {
    fun getTemperature() : Flow<TemperatureData>
}