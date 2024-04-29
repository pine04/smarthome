package au.edu.swin.sdmd.smarthome.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.edu.swin.sdmd.smarthome.R
import au.edu.swin.sdmd.smarthome.ui.theme.AppTheme

@Composable
fun TriggerCard(
    @StringRes triggerActionNameResource: Int,
    triggerTime: String,
    modifier: Modifier = Modifier,
    navigateToTriggerEdit: () -> Unit = { }
) {
    Card(
        modifier = modifier
            .clickable(onClickLabel = stringResource(R.string.go_to_list)) { navigateToTriggerEdit() }
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Column {
                Text(
                    text = stringResource(id = triggerActionNameResource),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = stringResource(id = R.string.trigger_time_at, triggerTime),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.chevron_right_24px),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Preview
@Composable
fun TriggerCardPreview() {
    AppTheme {
        TriggerCard(
            triggerActionNameResource = R.string.action_turn_on,
            triggerTime = "17:00"
        )
    }
}