package au.edu.swin.sdmd.smarthome.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.edu.swin.sdmd.smarthome.R
import au.edu.swin.sdmd.smarthome.data.airconditioner.AirConditioner
import au.edu.swin.sdmd.smarthome.ui.theme.AppTheme

// A card that can be clicked on to go to the air conditioner control screen.
@Composable
fun AirConditionerItem(
    airConditioner: AirConditioner,
    modifier: Modifier = Modifier,
    navigateToAirConditionerControls: (Int) -> Unit = { },
    toggleAirConditioner: (Boolean) -> Unit = { }
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClickLabel = stringResource(R.string.go_to_controls)) {
                navigateToAirConditionerControls(
                    airConditioner.id
                )
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = airConditioner.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = airConditioner.location,
                    style = MaterialTheme.typography.bodyMedium
                )

                Row(modifier = Modifier.padding(top = 8.dp)) {
                    Text(
                        text = stringResource(if (airConditioner.isOn) R.string.on else R.string.off),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    val temperature = "%.1f".format(airConditioner.temperature)
                    Text(
                        text = "Temperature $temperature°C",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

                    Text(
                        text = "Fan ${airConditioner.fanSpeed}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Switch(
                checked = airConditioner.isOn,
                onCheckedChange = { toggleAirConditioner(!airConditioner.isOn) },
                modifier = Modifier
                    .padding(start = 16.dp)

            )
        }
    }
}

@Preview
@Composable
fun AirConditionerItemPreview() {
    AppTheme {
        AirConditionerItem(
            airConditioner = AirConditioner(
                name = "Hallway Central AC",
                location = "Hallway"
            )
        )
    }
}