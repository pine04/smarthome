package au.edu.swin.sdmd.smarthome.data.sensor_light

import android.util.Log
import au.edu.swin.sdmd.smarthome.data.SensorData
import au.edu.swin.sdmd.smarthome.data.SensorDataSource
import au.edu.swin.sdmd.smarthome.data.SensorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import java.util.Date

class MQTTLightRepository (sensorDataSource: SensorDataSource): SensorRepository(sensorDataSource) {
    override fun getValue(): Flow<SensorData> {
        return sensorDataSource.messageFlow.filter { it.topic == "student/feeds/light" }
            .map {
                val data = it.message.substring(1, it.message.length - 1).split(",")

                Log.d("light repo", it.message)

                SensorData(
                    time = Date(data[0].toDouble().toLong() * 1000),
                    value = data[1].toInt()
                )
            }
    }
}