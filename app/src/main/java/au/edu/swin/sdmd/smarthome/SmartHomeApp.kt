package au.edu.swin.sdmd.smarthome

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import au.edu.swin.sdmd.smarthome.ui.airconditioner.AirConditionerControlsScreen
import au.edu.swin.sdmd.smarthome.ui.airconditioner.AirConditionerEditScreen
import au.edu.swin.sdmd.smarthome.ui.airconditioner.AirConditionersScreen
import au.edu.swin.sdmd.smarthome.ui.components.BottomNavigationBar
import au.edu.swin.sdmd.smarthome.ui.components.SmartHomeFloatingActionButton
import au.edu.swin.sdmd.smarthome.ui.home.HomeScreen
import au.edu.swin.sdmd.smarthome.ui.components.SmartHomeTopBar
import au.edu.swin.sdmd.smarthome.ui.devices.DeviceAddScreen
import au.edu.swin.sdmd.smarthome.ui.devices.DevicesScreen
import au.edu.swin.sdmd.smarthome.ui.light.LightControlsScreen
import au.edu.swin.sdmd.smarthome.ui.light.LightEditScreen
import au.edu.swin.sdmd.smarthome.ui.light.LightsScreen
import au.edu.swin.sdmd.smarthome.ui.rooms.RoomsScreen
import au.edu.swin.sdmd.smarthome.ui.rooms.SingleRoomScreen
import au.edu.swin.sdmd.smarthome.ui.settings.SettingsScreen

@Composable
fun SmartHomeApp(navController: NavHostController = rememberNavController()) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination =
        destinations.find { it.route == backStackEntry?.destination?.route?.split("/")?.get(0) }
            ?: HomeDestination

    Log.d("SmartHomeApp", SingleRoomDestination.routeWithArgs)

    Scaffold(
        topBar = {
            SmartHomeTopBar(currentDestination = currentDestination)
        },
        bottomBar = {
            BottomNavigationBar(
                currentDestination = currentDestination,
                navigateToDestination = { route: String ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                }
            )
        },
        floatingActionButton = {
            SmartHomeFloatingActionButton(
                currentDestination = currentDestination,
                navigateTo = { route: String -> navController.navigate(route) }
            )
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = HomeDestination.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            composable(HomeDestination.route) {
                HomeScreen(
                    navigateToLightControls = { id -> navController.navigate("light_controls/$id") },
                    navigateToAirConditionerControls = { id -> navController.navigate("air_conditioner_controls/$id") }
                )
            }

            composable(DevicesDestination.route) {
                DevicesScreen(
                    navigateToLights = { navController.navigate(LightsDestination.route) },
                    navigateToAirConditioners = { navController.navigate(AirConditionersDestination.route) }
                )
            }

            composable(LightsDestination.route) {
                LightsScreen(
                    navigateToLightControls = { id -> navController.navigate("light_controls/$id") }
                )
            }

            composable(AirConditionersDestination.route) {
                AirConditionersScreen(
                    navigateToAirConditionerControls = { id -> navController.navigate("air_conditioner_controls/$id") }
                )
            }

            composable(RoomsDestination.route) {
                RoomsScreen(
                    navigateToRoom = { room -> navController.navigate("single_room/$room") }
                )
            }

            composable(SettingsDestination.route) {
                SettingsScreen()
            }

            composable(DeviceAddDestination.route) {
                DeviceAddScreen(
                    navigateBack = { navController.navigateUp() }
                )
            }

            composable(
                route = LightControlsDestination.routeWithArgs,
                arguments = LightControlsDestination.arguments
            ) {
                LightControlsScreen(
                    navigateToLightEdit = { id: Int -> navController.navigate("light_edit/$id") }
                )
            }

            composable(
                route = LightEditDestination.routeWithArgs,
                arguments = LightEditDestination.arguments
            ) {
                LightEditScreen(
                    navigateBack = { navController.navigateUp() },
                    navigateToLights = { navController.navigate(LightsDestination.route) }
                )
            }

            composable(
                route = AirConditionerControlsDestination.routeWithArgs,
                arguments = AirConditionerControlsDestination.arguments
            ) {
                AirConditionerControlsScreen(
                    navigateToAirConditionerEdit = { id: Int -> navController.navigate("air_conditioner_edit/$id") }
                )
            }

            composable(
                route = AirConditionerEditDestination.routeWithArgs,
                arguments = AirConditionerEditDestination.arguments
            ) {
                AirConditionerEditScreen(
                    navigateBack = { navController.navigateUp() },
                    navigateToAirConditioners = { navController.navigate(AirConditionersDestination.route) }
                )
            }

            composable(
                route = SingleRoomDestination.routeWithArgs,
                arguments = SingleRoomDestination.arguments
            ) {
                SingleRoomScreen(
                    navigateToLightControls = { id -> navController.navigate("light_controls/$id") },
                    navigateToAirConditionerControls = { id -> navController.navigate("air_conditioner_controls/$id") }
                )
            }
        }
    }
}

interface NavigationDestination {
    val route: String
    val titleResId: Int
}

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleResId = R.string.home_title
}

object DevicesDestination : NavigationDestination {
    override val route = "devices"
    override val titleResId = R.string.devices_title
}

object DeviceAddDestination : NavigationDestination {
    override val route = "device_add"
    override val titleResId = R.string.add_a_device
}

object LightsDestination : NavigationDestination {
    override val route = "lights"
    override val titleResId = R.string.lights
}

object AirConditionersDestination : NavigationDestination {
    override val route = "air_conditioners"
    override val titleResId = R.string.air_conditioners
}

object RoomsDestination : NavigationDestination {
    override val route = "rooms"
    override val titleResId = R.string.rooms_title
}

object SingleRoomDestination : NavigationDestination {
    override val route = "single_room"
    override val titleResId = R.string.single_rooms_title
    const val roomArg = "room"
    val routeWithArgs = "$route/{$roomArg}"
    val arguments = listOf(
        navArgument(roomArg) { type = NavType.StringType }
    )
}

object SettingsDestination : NavigationDestination {
    override val route = "settings"
    override val titleResId = R.string.settings_title
}

object LightControlsDestination : NavigationDestination {
    override val route = "light_controls"
    override val titleResId = R.string.light_controls
    const val lightIdArg = "light_id"
    val routeWithArgs = "$route/{$lightIdArg}"
    val arguments = listOf(
        navArgument(lightIdArg) { type = NavType.IntType }
    )
}

object LightEditDestination : NavigationDestination {
    override val route = "light_edit"
    override val titleResId = R.string.light_edit
    const val lightIdArg = "light_id"
    val routeWithArgs = "$route/{$lightIdArg}"
    val arguments = listOf(
        navArgument(lightIdArg) { type = NavType.IntType }
    )
}

object AirConditionerControlsDestination : NavigationDestination {
    override val route = "air_conditioner_controls"
    override val titleResId = R.string.air_conditioner_controls
    const val airConditionerIdArg = "air_conditioner_id"
    val routeWithArgs = "$route/{$airConditionerIdArg}"
    val arguments = listOf(
        navArgument(airConditionerIdArg) { type = NavType.IntType }
    )
}

object AirConditionerEditDestination : NavigationDestination {
    override val route = "air_conditioner_edit"
    override val titleResId = R.string.air_conditioner_edit
    const val airConditionerIdArg = "air_conditioner_id"
    val routeWithArgs = "$route/{$airConditionerIdArg}"
    val arguments = listOf(
        navArgument(airConditionerIdArg) { type = NavType.IntType }
    )
}

val destinations = listOf(
    HomeDestination,
    DevicesDestination,
    DeviceAddDestination,
    LightsDestination,
    AirConditionersDestination,
    RoomsDestination,
    SingleRoomDestination,
    SettingsDestination,
    LightControlsDestination,
    LightEditDestination,
    AirConditionerControlsDestination,
    AirConditionerEditDestination
)