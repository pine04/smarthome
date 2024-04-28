package au.edu.swin.sdmd.smarthome.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter

abstract class SensorRepository (
    val sensorDataSource: SensorDataSource
) {
   abstract fun getValue(): Flow<SensorData>
}