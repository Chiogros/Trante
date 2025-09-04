package chiogros.cost

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import chiogros.cost.data.network.repository.NetworkManager
import chiogros.cost.data.network.sftp.LocalSftpNetworkDataSource
import chiogros.cost.data.network.sftp.RemoteSftpNetworkDataSource
import chiogros.cost.data.network.sftp.SftpNetwork
import chiogros.cost.data.network.sftp.SftpNetworkRepository
import chiogros.cost.data.room.AppDatabase
import chiogros.cost.data.room.repository.RoomManager
import chiogros.cost.data.room.sftp.SftpRoomDataSource
import chiogros.cost.data.room.sftp.SftpRoomRepository
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
        val sftpRoomDataSource =
            SftpRoomDataSource(AppDatabase.getDatabase(this).ConnectionSftpDao())
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
        val deleteConnectionUseCase = DeleteConnectionUseCase()
        val insertConnectionUseCase = InsertConnectionUseCase()
        val getConnectionsUseCase = GetConnectionsUseCase(roomManager)
        // View models
        val connectionsListViewModel = ConnectionsListViewModel(
            enableConnectionUseCase,
            disableConnectionUseCase,
            deleteConnectionUseCase,
            insertConnectionUseCase,
            getConnectionsUseCase
        )
        val connectionEditViewModel = ConnectionEditViewModel(roomManager)

        enableEdgeToEdge()
        setContent {
            EtomerTheme {
                Etomer(connectionsListViewModel, connectionEditViewModel)
            }
        }
    }
}