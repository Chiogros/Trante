package chiogros.etomer

import androidx.compose.animation.EnterTransition
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import chiogros.etomer.dao.DiceRollViewModel
import chiogros.etomer.dao.MyViewModel
import chiogros.etomer.ui.screens.ConnectionEdit
import chiogros.etomer.ui.screens.ConnectionsList
import kotlinx.serialization.Serializable

@Serializable
object ConnectionsList
@Serializable
object ConnectionEdit

@Composable
fun Etomer(viewmodel: MyViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ConnectionsList
    ) {
        composable<ConnectionsList> { ConnectionsList(
            onClick = { navController.navigate(ConnectionEdit) },
            viewModel = viewmodel
        ) }
        composable<ConnectionEdit> { ConnectionEdit( onClick = { navController.navigate(ConnectionsList) } ) }
    }
}
