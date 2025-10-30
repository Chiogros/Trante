package chiogros.trante.protocols.sftp.ui.ui.screens.connectionedit

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import chiogros.trante.R
import chiogros.trante.ui.ui.screens.connectionedit.ConnectionEditViewModel

@Composable
fun ConnectionEditForm(viewModel: ConnectionEditViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    OutlinedTextField(
        value = uiState.formState.host,
        onValueChange = { viewModel.setHost(it) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.host)) },
        placeholder = { Text(stringResource(R.string.example_dot_net)) },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None, autoCorrectEnabled = false
        ),
        singleLine = true
    )

    OutlinedTextField(
        value = uiState.formState.user,
        onValueChange = { viewModel.setUser(it) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.user)) },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None, autoCorrectEnabled = false
        ),
        singleLine = true
    )

    OutlinedTextField(
        value = uiState.formState.name,
        onValueChange = { viewModel.setName(it) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.name)) },
        placeholder = { Text(uiState.formState.host) },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences, autoCorrectEnabled = false
        ),
        singleLine = true
    )

    OutlinedTextField(
        value = uiState.formState.password,
        onValueChange = { viewModel.setPassword(it) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.password)) },
        placeholder = { Text(uiState.formState.password) },
        trailingIcon = {
            IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                Icon(
                    imageVector = if (uiState.showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = stringResource(R.string.show_password)
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrectEnabled = false,
            keyboardType = KeyboardType.Password
        ),
        visualTransformation = if (uiState.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        singleLine = true
    )
}