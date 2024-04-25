package au.edu.swin.sdmd.smarthome.data.sensor_humidity

import kotlinx.coroutines.flow.Flow

interface HumidityRepository {
    fun getHumidity() : Flow<HumidityData>
}