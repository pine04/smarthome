package au.edu.swin.sdmd.smarthome.data.sensor_temperature

import au.edu.swin.sdmd.smarthome.data.SensorDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import java.util.Date

class MQTTTemperatureSensorRepository(
    private val sensorDataSource: SensorDataSource
) : TemperatureSensorRepository {
    override fun getTemperatureData(): Flow<TemperatureSensorData> {
        return sensorDataSource.messageFlow.filter { it.topic == "temperatureSensor/data" }
            .map {
                val data = it.message.substring(1, it.message.length - 1).split(",")

                TemperatureSensorData(
                    time = Date(data[0].toDouble().toLong() * 1000),
                    temperature = data[1].toInt()
                )
            }
    }

    override fun getTemperatureSensorStatus(): Flow<Boolean> {
        return sensorDataSource.messageFlow.filter { it.topic == "temperatureSensor/status" }
            .map {
                it.message == "on"
            }
    }

    override fun turnOnSensor() {
        sensorDataSource.turnOnTemperatureSensor()
    }

    override fun turnOffSensor() {
        sensorDataSource.turnOffTemperatureSensor()
    }
}