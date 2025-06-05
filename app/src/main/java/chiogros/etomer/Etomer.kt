package chiogros.etomer

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import chiogros.etomer.ui.state.ConnectionEditViewModel
import chiogros.etomer.ui.state.ConnectionListViewModel
import chiogros.etomer.ui.ui.screens.ConnectionEdit
import chiogros.etomer.ui.ui.screens.ConnectionsList
import kotlinx.serialization.Serializable

@Serializable
object ConnectionsList
@Serializable
object ConnectionEdit

@Composable
fun Etomer(connectionListViewModel: ConnectionListViewModel, connectionEditViewModel: ConnectionEditViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ConnectionsList
    ) {
        composable<ConnectionEdit>(
            enterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Start) },
            popExitTransition = { slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.End) }
        ) { ConnectionEdit(
            onBack = { navController.popBackStack() },
            viewModel = connectionEditViewModel
        ) }
        composable<ConnectionsList> { ConnectionsList(
            onClick = { navController.navigate(ConnectionEdit) },
            viewModel = connectionListViewModel
        ) }
    }
}
