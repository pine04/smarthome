package au.edu.swin.sdmd.smarthome.ui.devices

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditioner
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditionerRepository
import au.edu.swin.sdmd.smarthome.data.light.Light
import au.edu.swin.sdmd.smarthome.data.light.OfflineLightRepository

val deviceOptions = arrayOf("Light", "Air conditioner")
val roomOptions =
    arrayOf("Living Room", "Bedroom", "Bathroom", "Kitchen", "Hallway", "Garage", "Attic")

// View model for the device add screen.
class DeviceAddViewModel(
    private val lightRepository: OfflineLightRepository,
    private val airConditionerRepository: AirConditionerRepository
) : ViewModel() {
    var uiState by mutableStateOf(DeviceAddUiState())
        private set

    fun updateDeviceDetails(details: DeviceDetails) {
        uiState = DeviceAddUiState(details, isValid(details))
    }

    private fun isValid(details: DeviceDetails): Boolean {
        with (details) {
            return deviceOptions.contains(deviceType) && roomOptions.contains(room) && deviceName.isNotBlank()
        }
    }

    suspend fun addDevice() {
        if (!uiState.isEntryValid) {
            return
        }
        
        with (uiState.deviceDetails) {
            if (deviceType == "Light") {
                lightRepository.insert(toLight())
            } else if (deviceType == "Air conditioner") {
                airConditionerRepository.insert(toAirConditioner())
            }
        }
    }
}

data class DeviceAddUiState(
    val deviceDetails: DeviceDetails = DeviceDetails(),
    val isEntryValid: Boolean = false
)

data class DeviceDetails(
    val deviceType: String = "",
    val room: String = "",
    val deviceName: String = "",
    val isFavorite: Boolean = false
)

fun DeviceDetails.toLight(): Light = Light(
    name = deviceName,
    location = room,
    isFavorite = isFavorite
)

fun DeviceDetails.toAirConditioner(): AirConditioner = AirConditioner(
    name = deviceName,
    location = room,
    isFavorite = isFavorite
)