package chiogros.etomer.ui.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material.icons.sharp.Check
import androidx.compose.material.icons.sharp.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
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
fun ConnectionEdit(onBack: () -> Unit, viewModel: ConnectionEditViewModel, id: Long? = null) {
    val uiState by viewModel.uiState.collectAsState()

    if (id == null) viewModel.refresh()
    else            viewModel.init(id)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { ConnectionEditTopBar(onBack, onBack, viewModel, onBack) }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(horizontal = 16.dp)) {
            ConnectionEditForm(viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionEditTopBar(onBack: () -> Unit, onSave: () -> Unit, viewModel: ConnectionEditViewModel, onDelete: () -> Unit) {
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
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Sharp.ArrowBack,
                    contentDescription = stringResource(R.string.get_back_previous_screen)
                )
            }
        },
        actions = {
            if (uiState.isEditing) {
                IconButton(
                    onClick = {
                        viewModel.delete()
                        onDelete()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Sharp.Delete,
                        contentDescription = stringResource(R.string.delete_connection),
                    )
                }
            }

            IconButton(
                onClick = {
                    if (uiState.isEditing)   viewModel.update()
                    else                     viewModel.insert()

                    onSave()
                },
                enabled = (!uiState.isEditing || (uiState.isEditing && uiState.isEdited))
            ) {
                Icon(
                    imageVector = Icons.Sharp.Check,
                    contentDescription = stringResource(R.string.save),
                )
            }
        }
    )
}

@Composable
fun ConnectionEditForm(viewModel: ConnectionEditViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    ConnectionEditTypePicker(viewModel)

    if (!uiState.type.isEmpty()) {
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

        OutlinedTextField(
            value = uiState.name,
            onValueChange = { viewModel.setName(it) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.name)) },
            placeholder = { Text(uiState.host) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                autoCorrectEnabled = false
            ),
            singleLine = true
        )
    }
}

@Composable
fun ConnectionEditTypePicker(viewModel: ConnectionEditViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val types = listOf<String>(ConnectionSftp.asString())

    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        types.forEachIndexed { index, label ->
            SegmentedButton(
                selected = (uiState.type == label),
                onClick = { viewModel.setType(label) },
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = types.size
                ),
                label = { Text(label) }
            )
        }
    }
}