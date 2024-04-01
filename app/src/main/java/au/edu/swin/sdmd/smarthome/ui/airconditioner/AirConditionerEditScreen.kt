package au.edu.swin.sdmd.smarthome.ui.airconditioner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.swin.sdmd.smarthome.AirConditionerEditDestination
import au.edu.swin.sdmd.smarthome.data.Room
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditioner
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditionerRepository
import au.edu.swin.sdmd.smarthome.ui.SmartHomeViewModelFactory
import au.edu.swin.sdmd.smarthome.ui.light.DeviceEditForm
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun AirConditionerEditScreen(
    navigateBack: () -> Unit,
    navigateToAirConditioners: () -> Unit,
    viewModel: AirConditionerEditViewModel = viewModel(factory = SmartHomeViewModelFactory)
) {
    val coroutineScope = rememberCoroutineScope()
    val airConditionerEditUiState = viewModel.airConditionerEditUiState
    val originalName = viewModel.originalName

    DeviceEditForm(
        originalName = originalName,
        deviceName = airConditionerEditUiState.airConditionerDetails.name,
        onDeviceNameChange = { name ->
            viewModel.updateAirConditionerDetails(airConditionerEditUiState.airConditionerDetails.copy(name = name))
        },
        room = airConditionerEditUiState.airConditionerDetails.location,
        onRoomChange = { room ->
            viewModel.updateAirConditionerDetails(airConditionerEditUiState.airConditionerDetails.copy(location = room))
        },
        isValid = viewModel.airConditionerEditUiState.isValid,
        onConfirmEdit = {
            coroutineScope.launch {
                viewModel.update()
                navigateBack()
            }
        },
        onConfirmDelete = {
            coroutineScope.launch {
                viewModel.delete()
                navigateToAirConditioners()
            }
        }
    )
}

class AirConditionerEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val airConditionerRepository: AirConditionerRepository
) : ViewModel() {

    var airConditionerEditUiState by mutableStateOf(AirConditionerEditUiState())
        private set

    var originalName by mutableStateOf("")
        private set

    private lateinit var originalAirConditioner: AirConditioner

    private val airConditionerId: Int = checkNotNull(savedStateHandle[AirConditionerEditDestination.airConditionerIdArg])

    init {
        viewModelScope.launch {
            originalAirConditioner = airConditionerRepository.getAirConditionerById(airConditionerId).filterNotNull().first()
            airConditionerEditUiState = originalAirConditioner.toAirConditionerEditUiState()
            originalName = originalAirConditioner.name
        }
    }

    fun updateAirConditionerDetails(airConditionerDetails: AirConditionerDetails) {
        airConditionerEditUiState = AirConditionerEditUiState(airConditionerDetails, isValid(airConditionerDetails))
    }

    suspend fun update() {
        airConditionerRepository.update(airConditionerEditUiState.airConditionerDetails.toAirConditioner(originalAirConditioner))
    }

    suspend fun delete() {
        airConditionerRepository.delete(originalAirConditioner)
    }

    private fun isValid(airConditionerDetails: AirConditionerDetails): Boolean {
        return airConditionerDetails.name.isNotBlank()
    }
}

data class AirConditionerEditUiState(
    val airConditionerDetails: AirConditionerDetails = AirConditionerDetails(),
    val isValid: Boolean = false
)

data class AirConditionerDetails(
    val name: String = "",
    val location: Room = Room.LIVING_ROOM
)

fun AirConditioner.toAirConditionerEditUiState(): AirConditionerEditUiState = AirConditionerEditUiState(
    airConditionerDetails = AirConditionerDetails(name = name, location = Room.entries.find { it.value == location } ?: Room.LIVING_ROOM),
    isValid = true
)

fun AirConditionerDetails.toAirConditioner(originalAirConditioner: AirConditioner): AirConditioner = originalAirConditioner.copy(
    name = name,
    location = location.value
)