package au.edu.swin.sdmd.smarthome

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.swin.sdmd.smarthome.data.Message
import au.edu.swin.sdmd.smarthome.data.SensorRepository
import au.edu.swin.sdmd.smarthome.data.sensor_temperature.TemperatureData
import au.edu.swin.sdmd.smarthome.data.sensor_temperature.TemperatureRepository
import au.edu.swin.sdmd.smarthome.ui.SmartHomeViewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.text.DateFormat
import java.util.Date

@Composable
fun MessageList(
    messageListViewModel: MessageListViewModel = viewModel(factory = SmartHomeViewModelFactory)
) {
    val temperature = messageListViewModel.temperature.collectAsState().value

    Text(text = "${android.icu.text.SimpleDateFormat("hh:mm:ss").format(temperature.time)}: ${temperature.temperature}")
}

class MessageListViewModel(
    temperatureRepository: TemperatureRepository
) : ViewModel() {
    val temperature: StateFlow<TemperatureData> = temperatureRepository.getTemperature().map { it }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = TemperatureData()
    )
}