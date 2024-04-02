package au.edu.swin.sdmd.smarthome.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.swin.sdmd.smarthome.data.UserPreferencesRepository
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditioner
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditionerRepository
import au.edu.swin.sdmd.smarthome.data.light.Light
import au.edu.swin.sdmd.smarthome.data.light.LightRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val lightRepository: LightRepository,
    private val airConditionerRepository: AirConditionerRepository,
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val uiState: StateFlow<HomeScreenUiState> = combine(
        userPreferencesRepository.preferences,
        airConditionerRepository.getFavorites(),
        lightRepository.getFavorites()
    ) { preferences, airConditioners, lights ->
        HomeScreenUiState(
            username = preferences.username,
            favoriteAirConditioners = airConditioners,
            favoriteLights = lights
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HomeScreenUiState()
    )

    suspend fun updateLight(light: Light) {
        lightRepository.update(light)
    }

    suspend fun updateAirConditioner(airConditioner: AirConditioner) {
        airConditionerRepository.update(airConditioner)
    }
}

data class HomeScreenUiState(
    val username: String = "User",
    val favoriteLights: List<Light> = listOf(),
    val favoriteAirConditioners: List<AirConditioner> = listOf()
)