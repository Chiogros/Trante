package chiogros.etomer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import chiogros.etomer.data.remote.repository.RemoteManager
import chiogros.etomer.data.remote.sftp.RemoteSftp
import chiogros.etomer.data.remote.sftp.RemoteSftpDataSource
import chiogros.etomer.data.remote.sftp.RemoteSftpRepository
import chiogros.etomer.data.room.AppDatabase
import chiogros.etomer.data.room.repository.ConnectionManager
import chiogros.etomer.data.room.sftp.ConnectionSftpRepository
import chiogros.etomer.data.room.sftp.ConnectionSftpRoomDataSource
import chiogros.etomer.domain.DeleteConnectionUseCase
import chiogros.etomer.domain.DisableConnectionUseCase
import chiogros.etomer.domain.EnableConnectionUseCase
import chiogros.etomer.domain.GetConnectionsUseCase
import chiogros.etomer.domain.InsertConnectionUseCase
import chiogros.etomer.ui.ui.screens.connectionedit.ConnectionEditViewModel
import chiogros.etomer.ui.ui.screens.connectionslist.ConnectionsListViewModel
import chiogros.etomer.ui.ui.theme.EtomerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        // Room
        val connectionSftpRoomDataSource =
            ConnectionSftpRoomDataSource(AppDatabase.getDatabase(this).ConnectionSftpDao())
        val connectionSftpRepository = ConnectionSftpRepository(connectionSftpRoomDataSource)
        val connectionManager = ConnectionManager(connectionSftpRepository)

        // Remote
        val remoteSftp = RemoteSftp()
        val remoteSftpRoomDataSource = RemoteSftpDataSource(remoteSftp)
        val remoteSftpRepository = RemoteSftpRepository(remoteSftpRoomDataSource)
        val remoteManager = RemoteManager(remoteSftpRepository)

        // Use cases
        val enableConnectionUseCase =
            EnableConnectionUseCase(connectionManager, remoteManager, this.applicationContext)
        val disableConnectionUseCase =
            DisableConnectionUseCase(connectionManager, this.applicationContext)
        val deleteConnectionUseCase = DeleteConnectionUseCase()
        val insertConnectionUseCase = InsertConnectionUseCase()
        val getConnectionsUseCase = GetConnectionsUseCase(connectionManager)

        // View models
        val connectionsListViewModel = ConnectionsListViewModel(
            enableConnectionUseCase,
            disableConnectionUseCase,
            deleteConnectionUseCase,
            insertConnectionUseCase,
            getConnectionsUseCase
        )
        val connectionEditViewModel = ConnectionEditViewModel(connectionManager)

        enableEdgeToEdge()
        setContent {
            EtomerTheme {
                Etomer(connectionsListViewModel, connectionEditViewModel)
            }
        }
    }
}