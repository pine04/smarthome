package au.edu.swin.sdmd.smarthome.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import au.edu.swin.sdmd.smarthome.MainApplication
import au.edu.swin.sdmd.smarthome.ui.airconditioner.AirConditionerEditViewModel
import au.edu.swin.sdmd.smarthome.ui.rooms.RoomsViewModel
import au.edu.swin.sdmd.smarthome.ui.rooms.SingleRoomViewModel
import au.edu.swin.sdmd.smarthome.ui.settings.SettingsViewModel
import au.edu.swin.sdmd.smarthome.ui.theme.AppThemeViewModel
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
val SmartHomeViewModelFactory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
        with(modelClass) {
            val application = checkNotNull(extras[APPLICATION_KEY]) as MainApplication
            val lightsRepository = application.container.lightRepository
            val airConditionersRepository = application.container.airConditionerRepository
            val userPreferencesRepository = application.container.userPreferencesRepository

            when {
                isAssignableFrom(AirConditionerEditViewModel::class.java) -> {
                    val stateHandle = extras.createSavedStateHandle()
                    AirConditionerEditViewModel(stateHandle, airConditionersRepository)
                }
                isAssignableFrom(RoomsViewModel::class.java) -> {
                    RoomsViewModel(lightsRepository, airConditionersRepository)
                }
                isAssignableFrom(SingleRoomViewModel::class.java) -> {
                    val stateHandle = extras.createSavedStateHandle()
                    SingleRoomViewModel(stateHandle, lightsRepository, airConditionersRepository)
                }
                isAssignableFrom(AppThemeViewModel::class.java) -> {
                    AppThemeViewModel(userPreferencesRepository)
                }
                isAssignableFrom(SettingsViewModel::class.java) -> {
                    SettingsViewModel(userPreferencesRepository)
                }
                else -> throw IllegalArgumentException("Invalid ViewModel class: ${modelClass.name}")
            }
        } as T
}