package au.edu.swin.sdmd.smarthome.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import au.edu.swin.sdmd.smarthome.MainApplication
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditioner
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditionerRepository
import au.edu.swin.sdmd.smarthome.data.light.Light
import au.edu.swin.sdmd.smarthome.data.light.LightRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HomeScreenViewModel(
    private val airConditionerRepository: AirConditionerRepository,
    private val lightRepository: LightRepository
) : ViewModel() {
    val uiState: StateFlow<HomeScreenUiState> =
        airConditionerRepository.getAllFavoriteAirConditioners().combine(
            lightRepository.getAllFavoriteLights()
        ) { airConditioners, lights ->
            HomeScreenUiState(favoriteAirConditioners = airConditioners, favoriteLights = lights)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HomeScreenUiState()
        )

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val container = (this[APPLICATION_KEY] as MainApplication).container
                val airConditionerRepository = container.airConditionerRepository
                val lightRepository = container.lightRepository
                HomeScreenViewModel(airConditionerRepository, lightRepository)
            }
        }
    }
}

data class HomeScreenUiState(
    val favoriteLights: List<Light> = listOf(),
    val favoriteAirConditioners: List<AirConditioner> = listOf()
)