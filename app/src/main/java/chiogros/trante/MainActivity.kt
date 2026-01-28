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
import chiogros.trante.protocols.common.CommonConnectionEditFormState
import chiogros.trante.protocols.sftp.SftpFactory
import chiogros.trante.protocols.sftp.data.network.SftpLocalNetworkDataSource
import chiogros.trante.protocols.sftp.data.network.SftpNetwork
import chiogros.trante.protocols.sftp.data.network.SftpNetworkRepository
import chiogros.trante.protocols.sftp.data.network.SftpRemoteNetworkDataSource
import chiogros.trante.protocols.sftp.data.room.SftpRoomDataSource
import chiogros.trante.protocols.sftp.data.room.SftpRoomRepository
import chiogros.trante.protocols.sftp.domain.SftpFormStateToRoomAdapter
import chiogros.trante.protocols.sftp.ui.ui.screens.connectionedit.SftpConnectionEditForm
import chiogros.trante.protocols.sftp.ui.ui.screens.connectionedit.SftpConnectionEditFormState
import chiogros.trante.protocols.sftp.ui.ui.screens.connectionedit.SftpConnectionEditViewModel
import chiogros.trante.ui.ui.screens.connectionedit.ConnectionEditViewModel
import chiogros.trante.ui.ui.screens.connectionslist.ConnectionsListViewModel
import chiogros.trante.ui.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        val dispatcher = Dispatchers.IO
        val context = this.applicationContext

        /**
         * SFTP
         */
        // Room
        val sftpConnectionDao = AppDatabase.getDatabase(context).connectionSftpDao()
        val sftpRoomDataSource = SftpRoomDataSource(sftpConnectionDao)
        val sftpRoomRepository = SftpRoomRepository(sftpRoomDataSource)
        // Remote
        val sftpNetwork = SftpNetwork.new(dispatcher)
        val sftpRemoteRoomDataSource = SftpRemoteNetworkDataSource(sftpNetwork)
        val sftpLocalNetworkDataSource = SftpLocalNetworkDataSource()
        val sftpNetworkRepository =
            SftpNetworkRepository(sftpRemoteRoomDataSource, sftpLocalNetworkDataSource)
        // View model
        val screenSftpConnectionEditFormState = MutableStateFlow(SftpConnectionEditFormState())
        val screenConnectionEditViewModel =
            SftpConnectionEditViewModel(screenSftpConnectionEditFormState)
        val screenConnectionEditForm: @Composable () -> Unit =
            { SftpConnectionEditForm(screenConnectionEditViewModel) }
        val formStateAdapter = SftpFormStateToRoomAdapter()

        // Protocols factories
        val sftpFactory = SftpFactory(
            networkRepository = sftpNetworkRepository,
            roomRepository = sftpRoomRepository,
            screensConnectionEditForm = screenConnectionEditForm,
            screensConnectionEditFormState = screenSftpConnectionEditFormState as MutableStateFlow<CommonConnectionEditFormState>,
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
            getConnectionsUseCase,
            getProtocolFromIdUseCase
        )
        val connectionEditViewModel = ConnectionEditViewModel(
            protocolFactoryManager = protocolFactoryManager,
            deleteConnectionUseCase = deleteConnectionUseCase,
            addConnectionUseCase = addConnectionUseCase,
            updateConnectionUseCase = updateConnectionUseCase,
            getProtocolFromIdUseCase = getProtocolFromIdUseCase
        )

        enableEdgeToEdge()
        setContent {
            AppTheme {
                App(connectionsListViewModel, connectionEditViewModel, protocolFactoryManager)
            }
        }
    }
}