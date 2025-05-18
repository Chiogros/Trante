package chiogros.etomer.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import chiogros.etomer.R

@Composable
fun ConnectionsList(onClick: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar() },
        floatingActionButton = { Fab(onClick) },
    ) { innerPadding -> ( innerPadding ) }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(stringResource(R.string.connections_list))
        }
    )
}

@Composable
fun Fab(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = onClick
    ) {
        Row {
            Icon(
                imageVector = Icons.Sharp.Add,
                contentDescription = stringResource(R.string.create_connection)
            )
            Text( text = stringResource(R.string.create_connection) )
        }
    }
}