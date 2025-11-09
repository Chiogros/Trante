package chiogros.trante.ui.ui.screens.connectionedit

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import chiogros.trante.R

@Composable
fun ConnectionEditCommonForm(viewModel: ConnectionEditViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    OutlinedTextField(
        value = uiState.formState.name,
        onValueChange = { viewModel.setName(it) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.name)) },
        placeholder = { Text(String()) },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences, autoCorrectEnabled = false
        ),
        singleLine = true
    )
}