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

@Composable
fun ConnectionEditFormSftp(viewModel: ConnectionEditViewModelSftp) {
    val uiState by viewModel.uiState.collectAsState()

    OutlinedTextField(
        value = uiState.host,
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
        value = uiState.user,
        onValueChange = { viewModel.setUser(it) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.user)) },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None, autoCorrectEnabled = false
        ),
        singleLine = true
    )

    OutlinedTextField(
        value = uiState.password,
        onValueChange = { viewModel.setPassword(it) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.password)) },
        placeholder = { Text(uiState.password) },
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