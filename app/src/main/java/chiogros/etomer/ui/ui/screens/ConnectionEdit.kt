package chiogros.etomer.ui.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
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
fun ConnectionEditTopBar(onBack: () -> Unit, onSave: () -> Unit, viewModel: ConnectionEditViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    TopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
        ),
        title = {
            Text(stringResource(R.string.create_connection))
        },
        navigationIcon = {
            IconButton(onClick = {
                viewModel.refreshAll()
                onBack()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Sharp.ArrowBack,
                    contentDescription = stringResource(R.string.get_back_previous_screen)
                )
            }
        },
        actions = {
            TextButton(
                onClick = {
                    viewModel.insert(ConnectionSftp(host = uiState.host, user = uiState.user))
                    viewModel.refreshAll()
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
    val uiState by viewModel.uiState.collectAsState()

    Row( modifier = Modifier.fillMaxWidth() ) { ConnectionEditTypePicker(viewModel) }

    if (uiState.type != "") {
        OutlinedTextField(
            value = uiState.host,
            onValueChange = { viewModel.setHost(it) },
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
            value = uiState.user,
            onValueChange = { viewModel.setUser(it) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.user)) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false
            ),
            singleLine = true
        )
    }
}

@Composable
fun ConnectionEditTypePicker(viewModel: ConnectionEditViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    FilterChip(
        onClick = { viewModel.setType( if (uiState.type != "SFTP") "SFTP" else "" ) },
        label = { Text(stringResource(R.string.sftp)) },
        modifier = Modifier.padding(start = 8.dp),
        selected = (uiState.type == "SFTP"),
        leadingIcon =
            if (uiState.type == "SFTP") {{
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )}
            } else { null }
    )
}