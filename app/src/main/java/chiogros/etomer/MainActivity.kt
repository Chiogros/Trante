package chiogros.etomer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import chiogros.etomer.data.datasource.room.ConnectionSftpRoomDataSource
import chiogros.etomer.data.repositories.room.ConnectionManager
import chiogros.etomer.data.repositories.room.ConnectionSftpRepository
import chiogros.etomer.data.room.AppDatabase
import chiogros.etomer.ui.ui.screens.connectionedit.ConnectionEditViewModel
import chiogros.etomer.ui.ui.screens.connectionslist.ConnectionsListViewModel
import chiogros.etomer.ui.ui.theme.EtomerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        val connectionSftpRoomDataSource =
            ConnectionSftpRoomDataSource(AppDatabase.getDatabase(this).ConnectionSftpDao())
        val connectionSftpRepository = ConnectionSftpRepository(connectionSftpRoomDataSource)

        val connectionManager = ConnectionManager(connectionSftpRepository)

        val connectionsListViewModel = ConnectionsListViewModel(connectionManager)
        val connectionEditViewModel = ConnectionEditViewModel(connectionManager)

        enableEdgeToEdge()
        setContent {
            EtomerTheme {
                Etomer(connectionsListViewModel, connectionEditViewModel)
            }
        }
    }
}