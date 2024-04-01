package au.edu.swin.sdmd.smarthome.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditioner
import au.edu.swin.sdmd.smarthome.data.light.Light

@Composable
fun HomeScreen(
    navigateToLightControls: (Int) -> Unit,
    navigateToAirConditionerControls: (Int) -> Unit,
    viewModel: HomeScreenViewModel = viewModel(factory = HomeScreenViewModel.Factory)
) {
    val uiState = viewModel.uiState.collectAsState()
    val favoriteLights = uiState.value.favoriteLights
    val favoriteAirConditioners = uiState.value.favoriteAirConditioners

    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Welcome, Quang Tung!"
            )
        }
        item {
            Text(text = "Lights")
        }

        items(favoriteLights) { light ->
            LightItem(
                light = light,
                navigateToLightControls = navigateToLightControls
            )
        }

        item {
            Text(text = "Air conditioners")
        }

        items(favoriteAirConditioners) { airConditioner ->
            AirConditionerItem(
                airConditioner = airConditioner,
                navigateToAirConditionerControls = navigateToAirConditionerControls
            )
        }
    }
}

@Composable
fun AirConditionerItem(
    airConditioner: AirConditioner,
    modifier: Modifier = Modifier,
    navigateToAirConditionerControls: (Int) -> Unit = { }
) {
    Card(modifier = modifier
        .fillMaxWidth()
        .clickable { navigateToAirConditionerControls(airConditioner.id) }) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = airConditioner.name, style = MaterialTheme.typography.displayMedium)
            Text(text = airConditioner.location)
            Text(text = if (airConditioner.isOn) "On" else "Off")
        }
    }
}

@Composable
fun LightItem(
    light: Light,
    modifier: Modifier = Modifier,
    navigateToLightControls: (Int) -> Unit = { }
) {
    Card(modifier = modifier
        .fillMaxWidth()
        .clickable { navigateToLightControls(light.id) }) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = light.name, style = MaterialTheme.typography.displayMedium)
            Text(text = light.location)
            Text(text = if (light.isOn) "On" else "Off")
        }
    }
}

@Preview
@Composable
fun AirConditionerItemPreview() {
    AirConditionerItem(airConditioner = AirConditioner(name = "Two-way Air Conditioner", location = "Bedroom"))
}