package au.edu.swin.sdmd.smarthome.data.sensor_humidity

import au.edu.swin.sdmd.smarthome.data.SensorData
import au.edu.swin.sdmd.smarthome.data.SensorDataSource
import au.edu.swin.sdmd.smarthome.data.SensorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import java.util.Date

class MQTTHumiditySensorRepository(
    private val sensorDataSource: SensorDataSource
) : SensorRepository {
    override fun getValue(): Flow<SensorData> {
        return sensorDataSource.messageFlow.filter { it.topic == "humiditySensor/data" }
            .map {
                val data = it.message.substring(1, it.message.length - 1).split(",")

                SensorData(
                    time = Date(data[0].toDouble().toLong() * 1000),
                    value = data[1].toInt()
                )
            }
    }

    override fun getStatus(): Flow<Boolean> {
        return sensorDataSource.messageFlow.filter { it.topic == "humiditySensor/status" }
            .map {
                it.message == "on"
            }
    }

    override fun turnOnSensor() {
        sensorDataSource.turnOnHumiditySensor()
    }

    override fun turnOffSensor() {
        sensorDataSource.turnOffHumiditySensor()
    }
}