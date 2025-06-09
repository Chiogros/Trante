package chiogros.etomer

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import chiogros.etomer.ui.state.ConnectionEditViewModel
import chiogros.etomer.ui.state.ConnectionListViewModel
import chiogros.etomer.ui.ui.screens.ConnectionEdit
import chiogros.etomer.ui.ui.screens.ConnectionsList
import kotlinx.serialization.Serializable

@Serializable
object ConnectionsList
@Serializable
data class ConnectionEdit(val connectionId: Long? = null)

@Composable
fun Etomer(connectionListViewModel: ConnectionListViewModel, connectionEditViewModel: ConnectionEditViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ConnectionsList,
        enterTransition = { EnterTransition.None },
    ) {
        composable<ConnectionEdit>(
            enterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Start) },
            popExitTransition = { slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.End) }
        ) { backStackEntry ->
            ConnectionEdit(
                onBack = { navController.popBackStack() },
                viewModel = connectionEditViewModel,
                id = backStackEntry.toRoute<ConnectionEdit>().connectionId
            )
        }

        composable<ConnectionsList> { ConnectionsList(
            onFabClick = { navController.navigate(ConnectionEdit()) },
            viewModel = connectionListViewModel,
            onItemClick = { id: Long -> navController.navigate(ConnectionEdit(id)) }
        ) }
    }
}
