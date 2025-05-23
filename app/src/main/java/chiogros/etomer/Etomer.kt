package chiogros.etomer

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import chiogros.etomer.ui.state.ConnectionViewModel
import chiogros.etomer.ui.ui.screens.ConnectionEdit
import chiogros.etomer.ui.ui.screens.ConnectionsList
import kotlinx.serialization.Serializable

@Serializable
object ConnectionsList
@Serializable
object ConnectionEdit

@Composable
fun Etomer(connectionViewModel: ConnectionViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ConnectionsList
    ) {
        composable<ConnectionEdit> { ConnectionEdit( onClick = { navController.navigate(ConnectionsList) } ) }
        composable<ConnectionsList> { ConnectionsList(
            onClick = { navController.navigate(ConnectionEdit) },
            viewModel = connectionViewModel
        ) }
    }
}
