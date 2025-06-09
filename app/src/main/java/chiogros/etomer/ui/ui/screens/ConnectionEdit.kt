package chiogros.etomer.ui.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.sharp.Check
import androidx.compose.material.icons.sharp.Delete
import androidx.compose.material3.AlertDialog
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
fun ConnectionEdit(onBack: () -> Unit, viewModel: ConnectionEditViewModel, id: Long? = null) {
    if (id == null) viewModel.refresh()
    else            viewModel.init(id)

    ConnectionEditDialog(viewModel, onBack)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { ConnectionEditTopBar(onBack, onBack, viewModel, onBack) }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(horizontal = 16.dp),
               verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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
            if (uiState.isEditing)  Text(stringResource(R.string.edit_connection))
            else                    Text(stringResource(R.string.create_connection))
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.cancel)
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
                enabled = (!uiState.isEditing || uiState.isEdited)
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

    if (!uiState.formState.type.isEmpty()) {
        OutlinedTextField(
            value = uiState.formState.host,
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
            value = uiState.formState.user,
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
            value = uiState.formState.name,
            onValueChange = { viewModel.setName(it) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.name)) },
            placeholder = { Text(uiState.formState.host) },
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
                selected = (uiState.formState.type == label),
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

@Composable
fun ConnectionEditDialog(viewModel: ConnectionEditViewModel, onSave: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()

    // If data is modified, when back button is pressed set dialog state
    BackHandler(enabled = uiState.isEdited, onBack = {
        viewModel.setIsDialogShown(true)
    })

    if (uiState.isDialogShown) {
        AlertDialog(
            onDismissRequest = { viewModel.setIsDialogShown(false) },
            icon = { Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = stringResource(R.string.warning)
            )},
            title = { Text(stringResource(R.string.changes_not_saved)) },
            text = { Text(stringResource(R.string.you_may_lost_your_changes)) },
            confirmButton = { TextButton(onClick = onSave) { Text(stringResource(R.string.continue_)) } },
            dismissButton = { TextButton(onClick = { viewModel.setIsDialogShown(false) }) { Text(stringResource(R.string.cancel)) } }
        )
    }
}