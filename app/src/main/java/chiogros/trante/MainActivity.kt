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
import chiogros.trante.domain.GetConnectionsUseCase
import chiogros.trante.domain.GetProtocolFromIdUseCase
import chiogros.trante.domain.NotifyContentResolverUseCase
import chiogros.trante.domain.UpdateConnectionUseCase
import chiogros.trante.protocols.ProtocolFactoryManager
import chiogros.trante.protocols.common.CommonConnectionEditFormState
import chiogros.trante.protocols.ftp.FtpFactory
import chiogros.trante.protocols.ftp.data.network.FtpLocalNetworkDataSource
import chiogros.trante.protocols.ftp.data.network.FtpNetwork
import chiogros.trante.protocols.ftp.data.network.FtpNetworkRepository
import chiogros.trante.protocols.ftp.data.network.FtpRemoteNetworkDataSource
import chiogros.trante.protocols.ftp.data.room.FtpRoomDataSource
import chiogros.trante.protocols.ftp.data.room.FtpRoomRepository
import chiogros.trante.protocols.ftp.domain.FtpFormStateToRoomAdapter
import chiogros.trante.protocols.ftp.ui.ui.screens.connectionedit.FtpConnectionEditForm
import chiogros.trante.protocols.ftp.ui.ui.screens.connectionedit.FtpConnectionEditFormState
import chiogros.trante.protocols.ftp.ui.ui.screens.connectionedit.FtpConnectionEditViewModel
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

        /////////
        // FTP //
        /////////
        // Room
        val ftpConnectionDao = AppDatabase.getDatabase(context).connectionFtpDao()
        val ftpRoomDataSource = FtpRoomDataSource(ftpConnectionDao)
        val ftpRoomRepository = FtpRoomRepository(ftpRoomDataSource)
        // Remote
        val ftpNetwork = FtpNetwork.new(dispatcher)
        val ftpRemoteRoomDataSource = FtpRemoteNetworkDataSource(ftpNetwork)
        val ftpLocalNetworkDataSource = FtpLocalNetworkDataSource()
        val ftpNetworkRepository =
            FtpNetworkRepository(ftpRemoteRoomDataSource, ftpLocalNetworkDataSource)
        // View model
        val ftpScreenConnectionEditFormState = MutableStateFlow(FtpConnectionEditFormState())
        val ftpScreenConnectionEditViewModel =
            FtpConnectionEditViewModel(ftpScreenConnectionEditFormState)
        val ftpScreenConnectionEditForm: @Composable () -> Unit =
            { FtpConnectionEditForm(ftpScreenConnectionEditViewModel) }
        val ftpFormStateAdapter = FtpFormStateToRoomAdapter()
        // Protocols factories
        val ftpFactory = FtpFactory(
            networkRepository = ftpNetworkRepository,
            roomRepository = ftpRoomRepository,
            screensConnectionEditForm = ftpScreenConnectionEditForm,
            screensConnectionEditFormState = ftpScreenConnectionEditFormState as MutableStateFlow<CommonConnectionEditFormState>,
            formStateRoomAdapter = ftpFormStateAdapter
        )

        //////////
        // SFTP //
        //////////
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
        val sftpScreenConnectionEditFormState = MutableStateFlow(SftpConnectionEditFormState())
        val sftpScreenConnectionEditViewModel =
            SftpConnectionEditViewModel(sftpScreenConnectionEditFormState)
        val sftpScreenConnectionEditForm: @Composable () -> Unit =
            { SftpConnectionEditForm(sftpScreenConnectionEditViewModel) }
        val formStateAdapter = SftpFormStateToRoomAdapter()
        // Protocols factories
        val sftpFactory = SftpFactory(
            networkRepository = sftpNetworkRepository,
            roomRepository = sftpRoomRepository,
            screensConnectionEditForm = sftpScreenConnectionEditForm,
            screensConnectionEditFormState = sftpScreenConnectionEditFormState as MutableStateFlow<CommonConnectionEditFormState>,
            formStateRoomAdapter = formStateAdapter
        )

        val protocolFactoryManager = ProtocolFactoryManager(sftpFactory, ftpFactory)

        // Use cases
        val notifyContentResolverUseCase = NotifyContentResolverUseCase(context)
        val enableConnectionUseCase =
            EnableConnectionUseCase(protocolFactoryManager, notifyContentResolverUseCase)
        val disableConnectionUseCase =
            DisableConnectionUseCase(protocolFactoryManager, notifyContentResolverUseCase)
        val getConnectionsUseCase = GetConnectionsUseCase(protocolFactoryManager)
        val deleteConnectionUseCase = DeleteConnectionUseCase(protocolFactoryManager)
        val addConnectionUseCase = AddConnectionUseCase(protocolFactoryManager)
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