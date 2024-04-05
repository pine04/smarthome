package au.edu.swin.sdmd.smarthome.ui.airconditioner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditioner
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditionerRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

// View model for the air conditioners screen.
class AirConditionersViewModel(
    private val airConditionerRepository: AirConditionerRepository
) : ViewModel() {
    val airConditionersUiState: StateFlow<AirConditionersUiState> = airConditionerRepository.getAll().map { airConditioners ->
        AirConditionersUiState(airConditioners)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = AirConditionersUiState()
    )

    suspend fun updateAirConditioner(airConditioner: AirConditioner) {
        airConditionerRepository.update(airConditioner)
    }
}

data class AirConditionersUiState(
    val airConditioners: List<AirConditioner> = listOf()
)