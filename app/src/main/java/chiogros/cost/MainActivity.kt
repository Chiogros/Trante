package chiogros.cost

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import chiogros.cost.data.remote.repository.RemoteManager
import chiogros.cost.data.remote.sftp.LocalSftpDataSource
import chiogros.cost.data.remote.sftp.RemoteSftp
import chiogros.cost.data.remote.sftp.RemoteSftpDataSource
import chiogros.cost.data.remote.sftp.RemoteSftpRepository
import chiogros.cost.data.room.AppDatabase
import chiogros.cost.data.room.repository.ConnectionManager
import chiogros.cost.data.room.sftp.ConnectionSftpRepository
import chiogros.cost.data.room.sftp.ConnectionSftpRoomDataSource
import chiogros.cost.domain.DeleteConnectionUseCase
import chiogros.cost.domain.DisableConnectionUseCase
import chiogros.cost.domain.EnableConnectionUseCase
import chiogros.cost.domain.GetConnectionsUseCase
import chiogros.cost.domain.InsertConnectionUseCase
import chiogros.cost.ui.ui.screens.connectionedit.ConnectionEditViewModel
import chiogros.cost.ui.ui.screens.connectionslist.ConnectionsListViewModel
import chiogros.cost.ui.ui.theme.EtomerTheme
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        val dispatcher = Dispatchers.IO

        // Room
        val connectionSftpRoomDataSource =
            ConnectionSftpRoomDataSource(AppDatabase.getDatabase(this).ConnectionSftpDao())
        val connectionSftpRepository = ConnectionSftpRepository(connectionSftpRoomDataSource)
        val connectionManager = ConnectionManager(connectionSftpRepository)

        // Remote
        val remoteSftpFactory = RemoteSftp.new(dispatcher)
        val remoteSftpRoomDataSource = RemoteSftpDataSource(remoteSftpFactory)
        val localSftpDataSource = LocalSftpDataSource(remoteSftpFactory)
        val remoteSftpRepository =
            RemoteSftpRepository(remoteSftpRoomDataSource, localSftpDataSource)
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