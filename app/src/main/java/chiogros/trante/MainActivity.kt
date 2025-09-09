package chiogros.trante

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import chiogros.trante.data.network.repository.NetworkManager
import chiogros.trante.data.network.sftp.LocalSftpNetworkDataSource
import chiogros.trante.data.network.sftp.RemoteSftpNetworkDataSource
import chiogros.trante.data.network.sftp.SftpNetwork
import chiogros.trante.data.network.sftp.SftpNetworkRepository
import chiogros.trante.data.room.AppDatabase
import chiogros.trante.data.room.repository.RoomManager
import chiogros.trante.data.room.sftp.SftpRoomDataSource
import chiogros.trante.data.room.sftp.SftpRoomRepository
import chiogros.trante.domain.DisableConnectionUseCase
import chiogros.trante.domain.EnableConnectionUseCase
import chiogros.trante.domain.GetConnectionsUseCase
import chiogros.trante.ui.ui.screens.connectionedit.ConnectionEditViewModel
import chiogros.trante.ui.ui.screens.connectionslist.ConnectionsListViewModel
import chiogros.trante.ui.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        val dispatcher = Dispatchers.IO

        // Room
        val sftpRoomDataSource =
            SftpRoomDataSource(AppDatabase.getDatabase(this).connectionSftpDao())
        val sftpRoomRepository = SftpRoomRepository(sftpRoomDataSource)
        val roomManager = RoomManager(sftpRoomRepository)

        // Remote
        val sftpNetworkFactory = SftpNetwork.new(dispatcher)
        val remoteSftpRoomDataSource = RemoteSftpNetworkDataSource(sftpNetworkFactory)
        val localSftpNetworkDataSource = LocalSftpNetworkDataSource()
        val sftpNetworkRepository =
            SftpNetworkRepository(remoteSftpRoomDataSource, localSftpNetworkDataSource)
        val networkManager = NetworkManager(sftpNetworkRepository)

        // Use cases
        val enableConnectionUseCase =
            EnableConnectionUseCase(roomManager, networkManager, this.applicationContext)
        val disableConnectionUseCase =
            DisableConnectionUseCase(roomManager, this.applicationContext)
        val getConnectionsUseCase = GetConnectionsUseCase(roomManager)

        // View models
        val connectionsListViewModel = ConnectionsListViewModel(
            enableConnectionUseCase,
            disableConnectionUseCase,
            getConnectionsUseCase
        )
        val connectionEditViewModel = ConnectionEditViewModel(roomManager)

        enableEdgeToEdge()
        setContent {
            AppTheme {
                App(connectionsListViewModel, connectionEditViewModel)
            }
        }
    }
}