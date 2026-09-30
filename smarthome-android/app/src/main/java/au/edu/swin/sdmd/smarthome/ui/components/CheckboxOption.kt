package au.edu.swin.sdmd.smarthome.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp

// A wrapper around a checkbox. Has text which can be clicked on to toggle the checkbox.
@Composable
fun CheckboxOption(
    selected: Boolean,
    optionText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable(
            role = Role.Checkbox,
            onClick = onClick,
            interactionSource = interactionSource,
            indication = null
        ).semantics {
            stateDescription = if (selected) {
                "Checked"
            } else {
                "Unchecked"
            }

            onClick(label = "check", action = null)
        }
    ) {
        Checkbox(
            checked = selected,
            onCheckedChange = null
        )
        Text(
            text = optionText,
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}