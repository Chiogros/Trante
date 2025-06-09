package chiogros.etomer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import chiogros.etomer.data.datasource.ConnectionSftpRoomDataSource
import chiogros.etomer.data.repositories.ConnectionSftpRepository
import chiogros.etomer.data.storage.AppDatabase
import chiogros.etomer.ui.state.ConnectionEditViewModel
import chiogros.etomer.ui.state.ConnectionListViewModel
import chiogros.etomer.ui.ui.theme.EtomerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        val connectionListViewModel = ConnectionListViewModel(
            ConnectionSftpRepository(
                ConnectionSftpRoomDataSource(
                    AppDatabase.getDatabase(this).ConnectionSftpDao()
                )
            )
        )
        val connectionEditViewModel = ConnectionEditViewModel(
            ConnectionSftpRepository(
                ConnectionSftpRoomDataSource(
                    AppDatabase.getDatabase(this).ConnectionSftpDao()
                )
            )
        )

        enableEdgeToEdge()
        setContent {
            EtomerTheme {
                Etomer(connectionListViewModel, connectionEditViewModel)
            }
        }
    }
}