package au.edu.swin.sdmd.smarthome.data

import android.content.Context
import android.util.Log
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import kotlin.coroutines.cancellation.CancellationException

class SensorDataSource(
    context: Context
) {
    val mqttClient = MqttAndroidClient(context, serverURI, clientId)

    init {
        val connectOptions = MqttConnectOptions().apply {
            isAutomaticReconnect = true
            isCleanSession = false
        }

        try {
            mqttClient.connect(connectOptions, context, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    val disconnectedBufferOptions = DisconnectedBufferOptions().apply {
                        isBufferEnabled = true
                        bufferSize = 100
                        isPersistBuffer = false
                        isDeleteOldestMessages = false
                    }

                    mqttClient.setBufferOpts(disconnectedBufferOptions)
                    subscribeToTopic(temperatureDataTopic)
                    subscribeToTopic(lightDataTopic)
                    subscribeToTopic(humidityDataTopic)
                    subscribeToTopic(temperatureStatusTopic)
                    subscribeToTopic(lightStatusTopic)
                    subscribeToTopic(humidityStatusTopic)
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d("Connect", "Failed to connect to server. Reason: ${exception!!.message}")
                }
            })
        } catch (exception: MqttException) {
            exception.printStackTrace()
        }
    }

    val messageFlow = callbackFlow {
        val mqttCallback = object : MqttCallbackExtended {
            override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                if (reconnect) {
                    Log.d("Callback", "Reconnected to server.")
                    subscribeToTopic(temperatureDataTopic)
                    subscribeToTopic(lightDataTopic)
                    subscribeToTopic(humidityDataTopic)
                    subscribeToTopic(temperatureStatusTopic)
                    subscribeToTopic(lightStatusTopic)
                    subscribeToTopic(humidityStatusTopic)
                } else {
                    Log.d("Callback", "Connected to server.")
                }
            }

            override fun connectionLost(cause: Throwable?) {
                cancel(CancellationException("Connection lost", cause))
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                Log.d("Callback", "Delivery completed")
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                Log.d("sensor flow", "$topic: ${message?.payload.toString()}")
                trySendBlocking(
                    Message(
                        topic = topic ?: "Unknown topic",
                        message = message?.payload?.let { String(it) } ?: "Empty message."
                    )
                ).onFailure { throwable ->
                    Log.d(
                        "Callback",
                        "Failed to send to flow: ${throwable?.message ?: "Unknown error"}"
                    )
                }
            }
        }

        mqttClient.setCallback(mqttCallback)

        awaitClose {
            close()
        }
    }.shareIn(
        scope = MainScope(),
        started = SharingStarted.Eagerly
    )

    private fun subscribeToTopic(topic: String) {
        mqttClient.subscribe(topic, 0, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d("Subscribe", "Subscribed to topic $topic")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.d("Subscribe", "Failed to subscribe to topic $topic")
            }
        })
    }

    fun turnOnTemperatureSensor() {
        val message = MqttMessage().apply {
            payload = "turn on".toByteArray()
            qos = 1
            isRetained = true
        }

        mqttClient.publish(temperatureActionTopic, message)
    }

    fun turnOffTemperatureSensor() {
        val message = MqttMessage().apply {
            payload = "turn off".toByteArray()
            qos = 1
            isRetained = true
        }

        mqttClient.publish(temperatureActionTopic, message)
    }

    fun turnOnHumiditySensor() {
        val message = MqttMessage().apply {
            payload = "turn on".toByteArray()
            qos = 1
            isRetained = true
        }

        mqttClient.publish(humidityActionTopic, message)
    }

    fun turnOffHumiditySensor() {
        val message = MqttMessage().apply {
            payload = "turn off".toByteArray()
            qos = 1
            isRetained = true
        }

        mqttClient.publish(humidityActionTopic, message)
    }

    fun turnOnLightSensor() {
        val message = MqttMessage().apply {
            payload = "turn on".toByteArray()
            qos = 1
            isRetained = true
        }

        mqttClient.publish(lightActionTopic, message)
    }

    fun turnOffLightSensor() {
        val message = MqttMessage().apply {
            payload = "turn off".toByteArray()
            qos = 1
            isRetained = true
        }

        mqttClient.publish(lightActionTopic, message)
    }

    companion object {
        private const val serverURI = "tcp://192.168.1.10:1883"
        private const val clientId = "smarthomeapp"
        private const val temperatureDataTopic = "temperatureSensor/data"
        private const val lightDataTopic = "lightSensor/data"
        private const val humidityDataTopic = "humiditySensor/data"
        private const val temperatureStatusTopic = "temperatureSensor/status"
        private const val lightStatusTopic = "lightSensor/status"
        private const val humidityStatusTopic = "humiditySensor/status"
        private const val temperatureActionTopic = "temperatureSensor/action"
        private const val lightActionTopic = "lightSensor/action"
        private const val humidityActionTopic = "humiditySensor/action"
    }
}

data class Message(
    val topic: String = "Unknown topic",
    val message: String = "Unknown message"
)