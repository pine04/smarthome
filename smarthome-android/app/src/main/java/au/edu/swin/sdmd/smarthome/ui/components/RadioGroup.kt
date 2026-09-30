package au.edu.swin.sdmd.smarthome.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import au.edu.swin.sdmd.smarthome.R

// A group of related radio options.
@Composable
fun RadioGroup(
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Column(
        modifier = modifier.selectableGroup(),
        content = content
    )
}

// Comprises a radio button and its accompanying text.
@Composable
fun RadioOption(
    selected: Boolean,
    optionText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val selectLabel = stringResource(id = R.string.select)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                interactionSource = interactionSource,
                indication = null,
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .semantics {
                onClick(
                    label = selectLabel,
                    action = null
                )
            }
    ) {
        RadioButton(
            selected = selected,
            onClick = null
        )

        Text(
            text = optionText,
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

