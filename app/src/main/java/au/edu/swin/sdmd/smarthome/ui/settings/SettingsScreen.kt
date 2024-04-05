package au.edu.swin.sdmd.smarthome.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import au.edu.swin.sdmd.smarthome.R
import au.edu.swin.sdmd.smarthome.data.allowedDarkModeOptions
import au.edu.swin.sdmd.smarthome.ui.SmartHomeViewModelFactory
import au.edu.swin.sdmd.smarthome.ui.components.RadioGroup
import au.edu.swin.sdmd.smarthome.ui.components.RadioOption
import kotlinx.coroutines.launch

// Screen to update the application's global settings.
@Composable
fun SettingsScreen(
    showSnackbarMessage: suspend (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = viewModel(factory = SmartHomeViewModelFactory)
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.dark_mode),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        RadioGroup {
            allowedDarkModeOptions.map { option ->
                RadioOption(
                    selected = viewModel.darkModeSettings == option,
                    optionText = option.replaceFirstChar { it.uppercase() },
                    onClick = {
                        viewModel.darkModeSettings = option
                        coroutineScope.launch {
                            viewModel.updateDarkModeSettings()
                            showSnackbarMessage("Dark mode settings updated to $option.")
                        }
                    }
                )
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        Text(
            text = "Username",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        TextField(
            value = viewModel.usernameSettings,
            onValueChange = { viewModel.usernameSettings = it },
            textStyle = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            enabled = viewModel.isUsernameValid(viewModel.usernameSettings),
            onClick = {
                coroutineScope.launch {
                    viewModel.updateUsername()
                    showSnackbarMessage("Username updated.")
                }
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.edit_username),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

