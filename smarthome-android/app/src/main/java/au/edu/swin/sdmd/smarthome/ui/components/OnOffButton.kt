package au.edu.swin.sdmd.smarthome.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.edu.swin.sdmd.smarthome.R
import au.edu.swin.sdmd.smarthome.ui.theme.AppTheme

// A button that can turn on or off a device.
@Composable
fun OnOffButton(
    isOn: Boolean,
    setIsOn: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonColors = if (isOn) {
        ButtonDefaults.buttonColors()
    } else {
        ButtonDefaults.filledTonalButtonColors()
    }

    Button(
        onClick = { setIsOn(!isOn) },
        modifier = modifier
            .size(192.dp)
            .semantics {
                stateDescription = if (isOn) {
                    "On"
                } else {
                    "Off"
                }

                onClick(
                    label = if (isOn) {
                        "Turn off"
                    } else {
                        "Turn on"
                    },
                    action = null
                )
            },
        colors = buttonColors,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.mode_off_on_24px),
            contentDescription = null,
            modifier = Modifier.size(96.dp)
        )
    }
}

@Preview
@Composable
fun OnOffButtonPreview() {
    AppTheme {
        OnOffButton(isOn = true, setIsOn = { })
    }
}