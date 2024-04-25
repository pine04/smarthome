package au.edu.swin.sdmd.smarthome.data.sensor_light

import kotlinx.coroutines.flow.Flow

interface LightRepository {
    fun getLight() : Flow<LightData>
}