package au.edu.swin.sdmd.smarthome.data.sensor_humidity

import au.edu.swin.sdmd.smarthome.data.SensorDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import java.util.Date

class MQTTHumiditySensorRepository(
    private val sensorDataSource: SensorDataSource
) : HumiditySensorRepository {
    override fun getHumidityData(): Flow<HumiditySensorData> {
        return sensorDataSource.messageFlow.filter { it.topic == "humiditySensor/data" }
            .map {
                val data = it.message.substring(1, it.message.length - 1).split(",")

                HumiditySensorData(
                    time = Date(data[0].toDouble().toLong() * 1000),
                    humidity = data[1].toInt()
                )
            }
    }

    override fun getHumiditySensorStatus(): Flow<Boolean> {
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