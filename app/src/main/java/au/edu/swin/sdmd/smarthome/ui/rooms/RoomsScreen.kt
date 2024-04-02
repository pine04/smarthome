package au.edu.swin.sdmd.smarthome.ui.rooms

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.swin.sdmd.smarthome.R
import au.edu.swin.sdmd.smarthome.data.Room
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditionerRepository
import au.edu.swin.sdmd.smarthome.data.light.LightRepository
import au.edu.swin.sdmd.smarthome.ui.SmartHomeViewModelFactory
import au.edu.swin.sdmd.smarthome.ui.home.HomeScreenUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@Composable
fun RoomsScreen(
    navigateToRoom: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RoomsViewModel = viewModel(factory = SmartHomeViewModelFactory)
) {
    val roomStates = viewModel.roomFlows.mapValues { it.value.collectAsState().value }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(16.dp)
    ) {
        items(rooms) { room ->
            RoomTab(
                roomIdentifier = room.identifier,
                iconResId = room.iconResId,
                stringResId = room.stringResId,
                activeDeviceCount = roomStates[room.identifier]?.active ?: 0,
                totalDeviceCount = roomStates[room.identifier]?.total ?: 0,
                navigateToRoom = navigateToRoom,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}

@Composable
fun RoomTab(
    roomIdentifier: String,
    @DrawableRes iconResId: Int,
    @StringRes stringResId: Int,
    activeDeviceCount: Int,
    totalDeviceCount: Int,
    navigateToRoom: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { navigateToRoom(roomIdentifier) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = "",
                modifier = Modifier.size(48.dp)
            )

            Text(
                text = stringResource(id = stringResId),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = if (totalDeviceCount == 0) {
                    "No devices in this room"
                } else {
                    "$activeDeviceCount/$totalDeviceCount device(s) active"
                },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

interface RoomTab {
    val identifier: String
    val iconResId: Int
    val stringResId: Int
}

object LivingRoom: RoomTab {
    override val identifier = "LivingRoom"
    override val iconResId = R.drawable.living_room_24px
    override val stringResId = R.string.living_room
}

object Bedroom: RoomTab {
    override val identifier = "Bedroom"
    override val iconResId = R.drawable.bedroom_24px
    override val stringResId = R.string.bedroom
}

object Bathroom: RoomTab {
    override val identifier = "Bathroom"
    override val iconResId = R.drawable.bathroom_24px
    override val stringResId = R.string.bathroom
}

object Kitchen: RoomTab {
    override val identifier = "Kitchen"
    override val iconResId = R.drawable.kitchen_24px
    override val stringResId = R.string.kitchen
}

object Hallway: RoomTab {
    override val identifier = "Hallway"
    override val iconResId = R.drawable.hallway_24px
    override val stringResId = R.string.hallway
}

object Garage: RoomTab {
    override val identifier = "Garage"
    override val iconResId = R.drawable.garage_24px
    override val stringResId = R.string.garage
}

object Attic: RoomTab {
    override val identifier = "Attic"
    override val iconResId = R.drawable.attic_24px
    override val stringResId = R.string.attic
}

val rooms = arrayOf(
    LivingRoom,
    Bedroom,
    Bathroom,
    Kitchen,
    Hallway,
    Garage,
    Attic
)
