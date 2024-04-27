package au.edu.swin.sdmd.smarthome.data.sensor_light

import au.edu.swin.sdmd.smarthome.data.SensorDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import java.util.Date

class MQTTLightSensorRepository(
    private val sensorDataSource: SensorDataSource
) : LightSensorRepository {
    override fun getLightData(): Flow<LightSensorData> {
        return sensorDataSource.messageFlow.filter { it.topic == "lightSensor/data" }
            .map {
                val data = it.message.substring(1, it.message.length - 1).split(",")

                LightSensorData(
                    time = Date(data[0].toDouble().toLong() * 1000),
                    light = data[1].toInt()
                )
            }
    }

    override fun getLightSensorStatus(): Flow<Boolean> {
        return sensorDataSource.messageFlow.filter { it.topic == "lightSensor/status" }
            .map {
                it.message == "on"
            }
    }

    override fun turnOnSensor() {
        sensorDataSource.turnOnLightSensor()
    }

    override fun turnOffSensor() {
        sensorDataSource.turnOffLightSensor()
    }
}