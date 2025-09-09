package chiogros.trante

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import chiogros.trante.ui.ui.screens.about.About
import chiogros.trante.ui.ui.screens.connectionedit.ConnectionEdit
import chiogros.trante.ui.ui.screens.connectionedit.ConnectionEditViewModel
import chiogros.trante.ui.ui.screens.connectionslist.ConnectionsList
import chiogros.trante.ui.ui.screens.connectionslist.ConnectionsListViewModel
import chiogros.trante.ui.ui.screens.license.ProjectLicense
import chiogros.trante.ui.ui.screens.license.ThirdPartyLicenses
import kotlinx.serialization.Serializable

@Serializable
object ConnectionsList

@Serializable
data class ConnectionEdit(val connectionId: String = "")

@Serializable
object About

@Serializable
object ProjectLicense

@Serializable
object ThirdPartyLicense

@Composable
fun App(
    connectionsListViewModel: ConnectionsListViewModel,
    connectionEditViewModel: ConnectionEditViewModel
) {
    val navController = rememberNavController()
    val snackbarHostState = SnackbarHostState()
    val coroutineScope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = ConnectionsList,
        enterTransition = {
            slideIn { IntOffset(it.width, 0) }
        },
        exitTransition = {
            slideOut { IntOffset(-it.width, 0) }
        },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = {
            scaleOut(
                targetScale = 0.85F,
                transformOrigin = TransformOrigin(pivotFractionX = 0.8f, pivotFractionY = 0.5f)
            ) + fadeOut(spring(stiffness = Spring.StiffnessMedium))
        },
    ) {
        composable<ConnectionsList> {
            ConnectionsList(
                onAboutClick = { navController.navigate(About) },
                onFabClick = { navController.navigate(ConnectionEdit()) },
                viewModel = connectionsListViewModel,
                onItemClick = { id: String -> navController.navigate(ConnectionEdit(id)) },
                snackbarHostState = snackbarHostState
            )
        }

        composable<ConnectionEdit> { backStackEntry ->
            ConnectionEdit(
                onBack = { navController.popBackStack() },
                viewModel = connectionEditViewModel,
                id = backStackEntry.toRoute<ConnectionEdit>().connectionId,
                snackbarHostState = snackbarHostState,
                coroutineScope = coroutineScope
            )
        }

        composable<About> { backStackEntry ->
            About(
                onBack = { navController.popBackStack() },
                onProjectLicenseClick = { navController.navigate(ProjectLicense) },
                onThirdPartyLicensesClick = { navController.navigate(ThirdPartyLicense) }
            )
        }

        composable<ProjectLicense> { backStackEntry ->
            ProjectLicense(
                onBack = { navController.popBackStack() }
            )
        }

        composable<ThirdPartyLicense> { backStackEntry ->
            ThirdPartyLicenses(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
