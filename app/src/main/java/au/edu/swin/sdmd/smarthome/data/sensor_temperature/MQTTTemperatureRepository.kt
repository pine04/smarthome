package au.edu.swin.sdmd.smarthome.data.sensor_temperature

import android.util.Log
import au.edu.swin.sdmd.smarthome.data.SensorDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import java.util.Date

class MQTTTemperatureRepository(
    private val sensorDataSource: SensorDataSource
) : TemperatureRepository {
    override fun getTemperature(): Flow<TemperatureData> {
        return sensorDataSource.messageFlow.filter { it.topic == "yourID/feeds/temperature" }
            .map {
                val data = it.message.substring(1, it.message.length - 1).split(",")

                Log.d("temperature repo", it.message)

                TemperatureData(
                    time = Date(data[0].toDouble().toLong() * 1000),
                    temperature = data[1].toInt()
                )
            }
    }
}