package chiogros.etomer.ui.ui.screens

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import chiogros.etomer.R
import chiogros.etomer.data.storage.ConnectionSftp
import chiogros.etomer.ui.state.ConnectionListViewModel

@Composable
fun ConnectionsList(
    onFabClick: () -> Unit,
    viewModel: ConnectionListViewModel,
    onItemClick: (Long) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val uiState by viewModel.uiState.collectAsState()
    val connections by uiState.connections.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { ConnectionsListTopBar() },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = { Fab(onFabClick) },
    ) { innerPadding ->
        if (connections.isEmpty()) {
            // Print a message if there is nothing to list
            Text(
                text = stringResource(R.string.no_connection) + "...",
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .wrapContentHeight(),
                textAlign = TextAlign.Center
            )
        } else {
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(items = connections, contentType = { it }) { connection ->
                    Item(connection, viewModel, onItemClick)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionsListTopBar() {
    val uriHandler = LocalUriHandler.current
    val repositoryUrl = stringResource(R.string.repository_url)

    TopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ), title = {
            Text(stringResource(R.string.connections_list))
        }, actions = {
            IconButton(onClick = { uriHandler.openUri(repositoryUrl) }) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = stringResource(R.string.app_info)
                )
            }
        })
}

@Composable
fun Fab(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = onClick
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Sharp.Add,
                contentDescription = stringResource(R.string.create_connection),
                modifier = Modifier
                    .width(24.dp)
                    .padding(end = 8.dp)
            )
            Text(text = stringResource(R.string.create_connection))
        }
    }
}

@Composable
fun Item(
    connection: ConnectionSftp, viewModel: ConnectionListViewModel, onItemClick: (Long) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = { onItemClick(connection.id) })
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = ConnectionSftp.asString(),
            modifier = Modifier.weight(1F),
            fontWeight = FontWeight.Normal
        )

        if (!connection.name.isEmpty()) {
            Text(text = connection.name, modifier = Modifier.weight(3F))
        } else {
            Column(modifier = Modifier.weight(3F)) {
                Text(
                    text = connection.host,
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(text = connection.user, style = MaterialTheme.typography.bodyMedium)
            }
        }

        Switch(
            checked = connection.enabled, onCheckedChange = {
                viewModel.toggle(connection)
            }, modifier = Modifier.weight(1F)
        )
    }
}