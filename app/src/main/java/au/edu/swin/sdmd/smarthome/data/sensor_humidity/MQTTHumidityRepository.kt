package au.edu.swin.sdmd.smarthome.data.sensor_humidity

import android.util.Log
import au.edu.swin.sdmd.smarthome.data.SensorDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import java.util.Date

class MQTTHumidityRepository(
    private val sensorDataSource: SensorDataSource
) : HumidityRepository {
    override fun getHumidity(): Flow<HumidityData> {
        return sensorDataSource.messageFlow.filter { it.topic == "yourID/feeds/humidity" }
            .map {
                val data = it.message.substring(1, it.message.length - 1).split(",")

                Log.d("humidity repo", it.message)

                HumidityData(
                    time = Date(data[0].toDouble().toLong() * 1000),
                    humidity = data[1].toInt()
                )
            }
    }
}