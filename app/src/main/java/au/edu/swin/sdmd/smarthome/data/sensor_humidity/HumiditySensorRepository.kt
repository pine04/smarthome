package au.edu.swin.sdmd.smarthome.data.sensor_humidity

import kotlinx.coroutines.flow.Flow

interface HumiditySensorRepository {
    fun getHumidityData() : Flow<HumiditySensorData>
    fun getHumiditySensorStatus() : Flow<Boolean>
    fun turnOnSensor()
    fun turnOffSensor()
}