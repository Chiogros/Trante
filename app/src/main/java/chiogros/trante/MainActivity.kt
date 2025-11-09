package chiogros.trante

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import chiogros.trante.data.room.AppDatabase
import chiogros.trante.domain.AddConnectionUseCase
import chiogros.trante.domain.DeleteConnectionUseCase
import chiogros.trante.domain.DisableConnectionUseCase
import chiogros.trante.domain.EnableConnectionUseCase
import chiogros.trante.domain.GetConnectionUseCase
import chiogros.trante.domain.GetConnectionsUseCase
import chiogros.trante.domain.GetProtocolFromIdUseCase
import chiogros.trante.domain.NotifyContentResolverUseCase
import chiogros.trante.domain.UpdateConnectionUseCase
import chiogros.trante.protocols.ProtocolFactoryManager
import chiogros.trante.protocols.sftp.SftpFactory
import chiogros.trante.protocols.sftp.data.network.LocalSftpNetworkDataSource
import chiogros.trante.protocols.sftp.data.network.RemoteSftpNetworkDataSource
import chiogros.trante.protocols.sftp.data.network.SftpNetwork
import chiogros.trante.protocols.sftp.data.network.SftpNetworkRepository
import chiogros.trante.protocols.sftp.data.room.SftpRoomDataSource
import chiogros.trante.protocols.sftp.data.room.SftpRoomRepository
import chiogros.trante.protocols.sftp.domain.FormStateToRoomAdapterSftp
import chiogros.trante.protocols.sftp.ui.ui.screens.connectionedit.ConnectionEditFormSftp
import chiogros.trante.protocols.sftp.ui.ui.screens.connectionedit.ConnectionEditFormStateSftp
import chiogros.trante.protocols.sftp.ui.ui.screens.connectionedit.ConnectionEditViewModelSftp
import chiogros.trante.ui.ui.screens.connectionedit.ConnectionEditViewModel
import chiogros.trante.ui.ui.screens.connectionslist.ConnectionsListViewModel
import chiogros.trante.ui.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        val dispatcher = Dispatchers.IO
        val context = this.applicationContext

        // Sftp
        val connectionSftpDao = AppDatabase.getDatabase(context).connectionSftpDao()
        val sftpRoomDataSource = SftpRoomDataSource(connectionSftpDao)
        val sftpRoomRepository = SftpRoomRepository(sftpRoomDataSource)
        // Remote
        val sftpNetwork = SftpNetwork.new(dispatcher)
        val remoteSftpRoomDataSource = RemoteSftpNetworkDataSource(sftpNetwork)
        val localSftpNetworkDataSource = LocalSftpNetworkDataSource()
        val sftpNetworkRepository =
            SftpNetworkRepository(remoteSftpRoomDataSource, localSftpNetworkDataSource)
        // View model
        val screenConnectionEditViewModel = ConnectionEditViewModelSftp()
        val screenConnectionEditForm: @Composable () -> Unit =
            { ConnectionEditFormSftp(screenConnectionEditViewModel) }
        val screenConnectionEditFormStateSftp = ConnectionEditFormStateSftp()
        val formStateAdapter = FormStateToRoomAdapterSftp()

        // Protocols factories
        val sftpFactory = SftpFactory(
            networkRepository = sftpNetworkRepository,
            roomRepository = sftpRoomRepository,
            screensConnectionEditForm = screenConnectionEditForm,
            screensConnectionEditCommonFormState = screenConnectionEditFormStateSftp,
            formStateRoomAdapter = formStateAdapter
        )
        val protocolFactoryManager = ProtocolFactoryManager(sftpFactory)

        // Use cases
        val notifyContentResolverUseCase = NotifyContentResolverUseCase(context)
        val enableConnectionUseCase =
            EnableConnectionUseCase(protocolFactoryManager, notifyContentResolverUseCase)
        val disableConnectionUseCase =
            DisableConnectionUseCase(protocolFactoryManager, notifyContentResolverUseCase)
        val getConnectionsUseCase = GetConnectionsUseCase(protocolFactoryManager)
        val deleteConnectionUseCase = DeleteConnectionUseCase(protocolFactoryManager)
        val addConnectionUseCase = AddConnectionUseCase(protocolFactoryManager)
        val getConnectionUseCase = GetConnectionUseCase(protocolFactoryManager)
        val updateConnectionUseCase = UpdateConnectionUseCase(protocolFactoryManager)
        val getProtocolFromIdUseCase = GetProtocolFromIdUseCase(protocolFactoryManager)

        // View models
        val connectionsListViewModel = ConnectionsListViewModel(
            enableConnectionUseCase,
            disableConnectionUseCase,
            getConnectionsUseCase
        )
        val connectionEditViewModel = ConnectionEditViewModel(
            protocolFactoryManager = protocolFactoryManager,
            deleteConnectionUseCase = deleteConnectionUseCase,
            addConnectionUseCase = addConnectionUseCase,
            getConnectionUseCase = getConnectionUseCase,
            updateConnectionUseCase = updateConnectionUseCase,
            getProtocolFromIdUseCase = getProtocolFromIdUseCase
        )

        enableEdgeToEdge()
        setContent {
            AppTheme {
                App(connectionsListViewModel, connectionEditViewModel)
            }
        }
    }
}