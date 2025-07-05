package chiogros.etomer

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import chiogros.etomer.ui.ui.screens.connectionedit.ConnectionEdit
import chiogros.etomer.ui.ui.screens.connectionedit.ConnectionEditViewModel
import chiogros.etomer.ui.ui.screens.connectionslist.ConnectionListViewModel
import chiogros.etomer.ui.ui.screens.connectionslist.ConnectionsList
import kotlinx.serialization.Serializable

@Serializable
object ConnectionsList

@Serializable
data class ConnectionEdit(val connectionId: String = "")

@Composable
fun Etomer(
    connectionListViewModel: ConnectionListViewModel,
    connectionEditViewModel: ConnectionEditViewModel
) {
    val navController = rememberNavController()
    val snackbarHostState = SnackbarHostState()
    val coroutineScope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = ConnectionsList,
        enterTransition = { EnterTransition.None },
    ) {
        composable<ConnectionEdit>(
            enterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Start) },
            popExitTransition = { slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.End) }) { backStackEntry ->
            ConnectionEdit(
                onBack = { navController.popBackStack() },
                viewModel = connectionEditViewModel,
                id = backStackEntry.toRoute<ConnectionEdit>().connectionId,
                snackbarHostState = snackbarHostState,
                coroutineScope = coroutineScope
            )
        }

        composable<ConnectionsList> {
            ConnectionsList(
                onFabClick = { navController.navigate(ConnectionEdit()) },
                viewModel = connectionListViewModel,
                onItemClick = { id: String -> navController.navigate(ConnectionEdit(id)) },
                snackbarHostState = snackbarHostState
            )
        }
    }
}
