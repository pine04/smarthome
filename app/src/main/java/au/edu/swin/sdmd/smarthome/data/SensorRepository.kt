package au.edu.swin.sdmd.smarthome.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter

interface SensorRepository {
   fun getValue(): Flow<SensorData>
   fun getStatus(): Flow<Boolean>
   fun turnOnSensor()
   fun turnOffSensor()
}