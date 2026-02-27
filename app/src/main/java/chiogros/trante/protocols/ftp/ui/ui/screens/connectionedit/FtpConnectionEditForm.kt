package chiogros.trante.protocols.ftp.ui.ui.screens.connectionedit

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
fun FtpConnectionEditForm(viewModel: FtpConnectionEditViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val showPassword by viewModel.showPassword.collectAsState()

    OutlinedTextField(
        value = uiState.name,
        onValueChange = { viewModel.setName(it) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.name)) },
        placeholder = { Text(String()) },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences, autoCorrectEnabled = false
        ),
        singleLine = true
    )

    OutlinedTextField(
        value = uiState.host,
        onValueChange = { viewModel.setHost(it) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.host) + stringResource(R.string.required_char)) },
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
        label = { Text(stringResource(R.string.user) + stringResource(R.string.required_char)) },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None, autoCorrectEnabled = false
        ),
        singleLine = true,
    )

    OutlinedTextField(
        value = uiState.password,
        onValueChange = { viewModel.setPassword(it) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.password) + stringResource(R.string.required_char)) },
        placeholder = { Text(uiState.password) },
        trailingIcon = {
            IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                if (showPassword) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = stringResource(R.string.hide_password)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.VisibilityOff,
                        contentDescription = stringResource(R.string.show_password)
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrectEnabled = false,
            keyboardType = KeyboardType.Password
        ),
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        singleLine = true,
    )
}