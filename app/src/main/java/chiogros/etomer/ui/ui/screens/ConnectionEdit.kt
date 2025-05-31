package chiogros.etomer.ui.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import chiogros.etomer.R
import chiogros.etomer.data.storage.ConnectionSftp
import chiogros.etomer.ui.state.ConnectionEditViewModel

@Composable
fun ConnectionEdit(onBack: () -> Unit, viewModel: ConnectionEditViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { ConnectionEditTopBar(onBack, onBack, viewModel) }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            ConnectionEditForm(viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionEditTopBar(onClick: () -> Unit, onSave: () -> Unit, viewModel: ConnectionEditViewModel) {
    TopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
        ),
        title = {
            Text(stringResource(R.string.create_connection))
        },
        navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Sharp.ArrowBack,
                    contentDescription = stringResource(R.string.get_back_previous_screen)
                )
            }
        },
        actions = {
            TextButton(
                onClick = {
                    viewModel.insert(ConnectionSftp(host = viewModel.uiState.value.host, user = viewModel.uiState.value.user))
                    onSave()
                }
            ) {
                Text(stringResource(R.string.save))
            }
        }
    )
}

@Composable
fun ConnectionEditForm(viewModel: ConnectionEditViewModel) {
    var host by remember { mutableStateOf("") }
    var user by remember { mutableStateOf("") }

    OutlinedTextField(
        value = host,
        onValueChange = {
            host = it
            viewModel.uiState.value.host = it
        },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.host)) },
        placeholder = { Text(stringResource(R.string.example_dot_net)) },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrectEnabled = false
        ),
        singleLine = true
    )

    OutlinedTextField(
        value = user,
        onValueChange = {
            user = it
            viewModel.uiState.value.user = it
        },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.user)) },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrectEnabled = false
        ),
        singleLine = true
    )
}