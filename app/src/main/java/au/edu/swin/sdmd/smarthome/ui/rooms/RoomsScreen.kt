package au.edu.swin.sdmd.smarthome.ui.rooms

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.swin.sdmd.smarthome.R
import au.edu.swin.sdmd.smarthome.ui.SmartHomeViewModelFactory

// Screen that displays a list of rooms that the user can go to.
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
                roomIdentifier = room.destination,
                iconResId = room.iconResId,
                stringResId = room.stringResId,
                activeDeviceCount = roomStates[room.destination]?.active ?: 0,
                totalDeviceCount = roomStates[room.destination]?.total ?: 0,
                navigateToRoom = navigateToRoom,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}

// Represents a clickable tab that takes the user to the corresponding room.
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
        modifier = modifier.clickable(onClickLabel = stringResource(R.string.view_devices_in_room)) {
            navigateToRoom(
                roomIdentifier
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )

            Text(
                text = stringResource(id = stringResId),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = if (totalDeviceCount == 0) {
                    stringResource(R.string.no_devices_in_this_room)
                } else {
                    stringResource(R.string.of_device_s_active, activeDeviceCount, totalDeviceCount)
                },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

interface RoomTab {
    val destination: String
    val roomSearchArgument: String
    val iconResId: Int
    val stringResId: Int
}

object LivingRoom : RoomTab {
    override val destination = "LivingRoom"
    override val roomSearchArgument = "Living Room"
    override val iconResId = R.drawable.living_room_24px
    override val stringResId = R.string.living_room
}

object Bedroom : RoomTab {
    override val destination = "Bedroom"
    override val roomSearchArgument = "Bedroom"
    override val iconResId = R.drawable.bedroom_24px
    override val stringResId = R.string.bedroom
}

object Bathroom : RoomTab {
    override val destination = "Bathroom"
    override val roomSearchArgument = "Bathroom"
    override val iconResId = R.drawable.bathroom_24px
    override val stringResId = R.string.bathroom
}

object Kitchen : RoomTab {
    override val destination = "Kitchen"
    override val roomSearchArgument = "Kitchen"
    override val iconResId = R.drawable.kitchen_24px
    override val stringResId = R.string.kitchen
}

object Hallway : RoomTab {
    override val destination = "Hallway"
    override val roomSearchArgument = "Hallway"
    override val iconResId = R.drawable.hallway_24px
    override val stringResId = R.string.hallway
}

object Garage : RoomTab {
    override val destination = "Garage"
    override val roomSearchArgument = "Garage"
    override val iconResId = R.drawable.garage_24px
    override val stringResId = R.string.garage
}

object Attic : RoomTab {
    override val destination = "Attic"
    override val roomSearchArgument = "Attic"
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
