package au.edu.swin.sdmd.smarthome.data.sensor_light

import android.util.Log
import au.edu.swin.sdmd.smarthome.data.SensorDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import java.util.Date

class MQTTLightRepository(
    private val sensorDataSource: SensorDataSource
) : LightRepository {
    override fun getLight(): Flow<LightData> {
        return sensorDataSource.messageFlow.filter { it.topic == "yourID/feeds/light" }
            .map {
                val data = it.message.substring(1, it.message.length - 1).split(",")

                Log.d("light repo", it.message)

                LightData(
                    time = Date(data[0].toDouble().toLong() * 1000),
                    light = data[1].toInt()
                )
            }
    }
}