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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.edu.swin.sdmd.smarthome.data.light.Light
import au.edu.swin.sdmd.smarthome.ui.theme.AppTheme

// A card that can be clicked on to go to the light control screen.
@Composable
fun LightItem(
    light: Light,
    modifier: Modifier = Modifier,
    navigateToLightControls: (Int) -> Unit = { },
    toggleLight: (Boolean) -> Unit = { }
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { navigateToLightControls(light.id) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = light.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = light.location,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Switch(
                checked = light.isOn,
                onCheckedChange = { toggleLight(!light.isOn) },
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Preview
@Composable
fun LightItemPreview() {
    AppTheme {
        LightItem(
            light = Light(
                name = "Nightlight Is A Very Pretty Light Next To My Bed",
                location = "Bedroom"
            )
        )
    }
}