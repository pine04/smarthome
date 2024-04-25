package au.edu.swin.sdmd.smarthome

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.navigation.NavGraph.Companion.findStartDestination
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
import au.edu.swin.sdmd.smarthome.ui.components.SmartHomeTopBar
import au.edu.swin.sdmd.smarthome.ui.devices.DeviceAddScreen
import au.edu.swin.sdmd.smarthome.ui.devices.DevicesScreen
import au.edu.swin.sdmd.smarthome.ui.home.HomeScreen
import au.edu.swin.sdmd.smarthome.ui.light.LightControlsScreen
import au.edu.swin.sdmd.smarthome.ui.light.LightEditScreen
import au.edu.swin.sdmd.smarthome.ui.light.LightsScreen
import au.edu.swin.sdmd.smarthome.ui.rooms.RoomsScreen
import au.edu.swin.sdmd.smarthome.ui.rooms.SingleRoomScreen
import au.edu.swin.sdmd.smarthome.ui.settings.SettingsScreen

// An EnterTransition object used for animating a screen when it comes into view.
val FadeInEnterTransition: EnterTransition = fadeIn(
    animationSpec = tween(
        300, easing = LinearEasing
    )
)

// An ExitTransition object used for animating a screen when it exits view.
val FadeOutExitTransition: ExitTransition = fadeOut(
    animationSpec = tween(
        300, easing = LinearEasing
    )
)

// The application's navigation graph.
@Composable
fun SmartHomeNavGraph(navController: NavHostController = rememberNavController()) {
    val snackbarHostState = remember { SnackbarHostState() }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination =
        destinations.find { it.route == backStackEntry?.destination?.route?.split("/")?.get(0) }
            ?: HomeDestination

    suspend fun showSnackbarMessage(message: String) {
        snackbarHostState.showSnackbar(
            message = message,
            withDismissAction = true
        )
    }

    Scaffold(
        topBar = {
            SmartHomeTopBar(
                currentDestination = currentDestination,
                navigateUp = { navController.navigateUp() }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentDestination = currentDestination,
                navigateToDestination = { route: String ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState, modifier = Modifier.clearAndSetSemantics {  }) },
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
            enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                )
            },
            popEnterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                )
            },
            popExitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            composable(
                route = HomeDestination.route,
                enterTransition = { FadeInEnterTransition },
                exitTransition = { FadeOutExitTransition },
                popEnterTransition = { FadeInEnterTransition },
                popExitTransition = { FadeOutExitTransition }
            ) {
                HomeScreen(
                    navigateToLightControls = { id -> navController.navigate("light_controls/$id") },
                    navigateToAirConditionerControls = { id -> navController.navigate("air_conditioner_controls/$id") },
                    navigateToLightsScreen = { navController.navigate(LightsDestination.route) },
                    navigateToAirConditionersScreen = { navController.navigate(AirConditionersDestination.route) }
                )
            }

            composable(
                route = DevicesDestination.route,
                enterTransition = { FadeInEnterTransition },
                exitTransition = { FadeOutExitTransition },
                popEnterTransition = { FadeInEnterTransition },
                popExitTransition = { FadeOutExitTransition }
            ) {
                DevicesScreen(
                    navigateToLights = { navController.navigate(LightsDestination.route) },
                    navigateToAirConditioners = { navController.navigate(AirConditionersDestination.route) }
                )
            }

            composable(
                route = LightsDestination.route,
            ) {
                LightsScreen(
                    navigateToLightControls = { id -> navController.navigate("light_controls/$id") }
                )
            }

            composable(AirConditionersDestination.route) {
                AirConditionersScreen(
                    navigateToAirConditionerControls = { id -> navController.navigate("air_conditioner_controls/$id") }
                )
            }

            composable(
                route = RoomsDestination.route,
                enterTransition = { FadeInEnterTransition },
                exitTransition = { FadeOutExitTransition },
                popEnterTransition = { FadeInEnterTransition },
                popExitTransition = { FadeOutExitTransition }
            ) {
                RoomsScreen(
                    navigateToRoom = { room -> navController.navigate("single_room/$room") }
                )
            }

            composable(
                route = SettingsDestination.route,
                enterTransition = { FadeInEnterTransition },
                exitTransition = { FadeOutExitTransition },
                popEnterTransition = { FadeInEnterTransition },
                popExitTransition = { FadeOutExitTransition }
            ) {
                SettingsScreen(
                    showSnackbarMessage = ::showSnackbarMessage
                )
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
                    navigateToLightEdit = { id: Int -> navController.navigate("light_edit/$id") },
                    showSnackbarMessage = ::showSnackbarMessage
                )
            }

            composable(
                route = LightEditDestination.routeWithArgs,
                arguments = LightEditDestination.arguments
            ) {
                LightEditScreen(
                    navigateBackAfterEdit = { navController.navigateUp() },
                    navigateBackAfterDelete = { navController.popBackStack(route = LightControlsDestination.routeWithArgs, inclusive = true) }
                )
            }

            composable(
                route = AirConditionerControlsDestination.routeWithArgs,
                arguments = AirConditionerControlsDestination.arguments
            ) {
                AirConditionerControlsScreen(
                    navigateToAirConditionerEdit = { id: Int -> navController.navigate("air_conditioner_edit/$id") },
                    showSnackbarMessage = ::showSnackbarMessage
                )
            }

            composable(
                route = AirConditionerEditDestination.routeWithArgs,
                arguments = AirConditionerEditDestination.arguments
            ) {
                AirConditionerEditScreen(
                    navigateBackAfterEdit = { navController.navigateUp() },
                    navigateBackAfterDelete = { navController.popBackStack(route = AirConditionerControlsDestination.routeWithArgs, inclusive = true) }
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

// Interface for the objects representing the routes for screens.
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

object MessageDestination : NavigationDestination {
    override val route = "messages"
    override val titleResId = R.string.messages
}

// An array containing the destination objects defined above.
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
    AirConditionerEditDestination,
    MessageDestination
)