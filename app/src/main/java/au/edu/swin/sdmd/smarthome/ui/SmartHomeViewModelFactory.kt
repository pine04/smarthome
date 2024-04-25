package au.edu.swin.sdmd.smarthome.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import au.edu.swin.sdmd.smarthome.AppThemeViewModel
import au.edu.swin.sdmd.smarthome.MainApplication
import au.edu.swin.sdmd.smarthome.MessageList
import au.edu.swin.sdmd.smarthome.MessageListViewModel
import au.edu.swin.sdmd.smarthome.ui.airconditioner.AirConditionerControlsViewModel
import au.edu.swin.sdmd.smarthome.ui.airconditioner.AirConditionerEditViewModel
import au.edu.swin.sdmd.smarthome.ui.airconditioner.AirConditionersViewModel
import au.edu.swin.sdmd.smarthome.ui.devices.DeviceAddViewModel
import au.edu.swin.sdmd.smarthome.ui.devices.DevicesViewModel
import au.edu.swin.sdmd.smarthome.ui.home.HomeViewModel
import au.edu.swin.sdmd.smarthome.ui.light.LightControlsViewModel
import au.edu.swin.sdmd.smarthome.ui.light.LightEditViewModel
import au.edu.swin.sdmd.smarthome.ui.light.LightsViewModel
import au.edu.swin.sdmd.smarthome.ui.rooms.RoomsViewModel
import au.edu.swin.sdmd.smarthome.ui.rooms.SingleRoomViewModel
import au.edu.swin.sdmd.smarthome.ui.settings.SettingsViewModel

// A Factory object used to create ViewModels. This factory is needed to add parameters to the view models' classes' constructors.
@Suppress("UNCHECKED_CAST")
val SmartHomeViewModelFactory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
        with(modelClass) {
            val application = checkNotNull(extras[APPLICATION_KEY]) as MainApplication

            val stateHandle = extras.createSavedStateHandle()
            val lightRepository = application.container.lightRepository
            val airConditionersRepository = application.container.airConditionerRepository
            val userPreferencesRepository = application.container.userPreferencesRepository
            val temperatureRepository = application.container.temperatureRepository
            val humidityRepository = application.container.humidityRepository
            val lightSensorRepository = application.container.lightSensorRepository

            when {
                isAssignableFrom(HomeViewModel::class.java) -> {
                    HomeViewModel(
                        lightRepository,
                        airConditionersRepository,
                        temperatureRepository,
                        humidityRepository,
                        lightSensorRepository,
                        userPreferencesRepository
                    )
                }

                isAssignableFrom(DevicesViewModel::class.java) -> {
                    DevicesViewModel(
                        lightRepository,
                        airConditionersRepository
                    )
                }

                isAssignableFrom(RoomsViewModel::class.java) -> {
                    RoomsViewModel(
                        lightRepository,
                        airConditionersRepository
                    )
                }

                isAssignableFrom(SettingsViewModel::class.java) -> {
                    SettingsViewModel(userPreferencesRepository)
                }

                isAssignableFrom(DeviceAddViewModel::class.java) -> {
                    DeviceAddViewModel(
                        lightRepository,
                        airConditionersRepository
                    )
                }

                isAssignableFrom(LightsViewModel::class.java) -> {
                    LightsViewModel(lightRepository)
                }

                isAssignableFrom(AirConditionersViewModel::class.java) -> {
                    AirConditionersViewModel(airConditionersRepository)
                }

                isAssignableFrom(LightControlsViewModel::class.java) -> {
                    LightControlsViewModel(
                        stateHandle,
                        lightRepository
                    )
                }

                isAssignableFrom(AirConditionerControlsViewModel::class.java) -> {
                    AirConditionerControlsViewModel(
                        stateHandle,
                        airConditionersRepository
                    )
                }

                isAssignableFrom(LightEditViewModel::class.java) -> {
                    LightEditViewModel(
                        stateHandle,
                        lightRepository
                    )
                }

                isAssignableFrom(AirConditionerEditViewModel::class.java) -> {
                    AirConditionerEditViewModel(
                        stateHandle,
                        airConditionersRepository
                    )
                }

                isAssignableFrom(SingleRoomViewModel::class.java) -> {
                    SingleRoomViewModel(
                        stateHandle,
                        lightRepository,
                        airConditionersRepository
                    )
                }

                isAssignableFrom(AppThemeViewModel::class.java) -> {
                    AppThemeViewModel(userPreferencesRepository)
                }

                isAssignableFrom(MessageListViewModel::class.java) -> {
                    MessageListViewModel(temperatureRepository)
                }

                else -> throw IllegalArgumentException("Invalid ViewModel class: ${modelClass.name}")
            }
        } as T
}