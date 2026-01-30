package chiogros.trante.ui.saf

import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import android.provider.DocumentsProvider
import androidx.compose.runtime.Composable
import chiogros.trante.data.room.AppDatabase
import chiogros.trante.domain.CreateFileUseCase
import chiogros.trante.domain.GetEnabledConnectionsUseCase
import chiogros.trante.domain.GetFileStatUseCase
import chiogros.trante.domain.GetProtocolFromIdUseCase
import chiogros.trante.domain.ListFilesInDirectoryUseCase
import chiogros.trante.domain.ReadFileUseCase
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow

class CustomDocumentsProvider : DocumentsProvider() {
    lateinit var createFileUseCase: CreateFileUseCase
    lateinit var getEnabledConnectionsUseCase: GetEnabledConnectionsUseCase
    lateinit var getFileStatUseCase: GetFileStatUseCase
    lateinit var listFilesInDirectoryUseCase: ListFilesInDirectoryUseCase
    lateinit var readFileUseCase: ReadFileUseCase
    lateinit var viewModel: CustomDocumentProviderViewModel
    private val dispatcher = Dispatchers.IO

    fun init(context: Context) {
        /**
         * FTP
         */
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
        val getProtocolFromIdUseCase = GetProtocolFromIdUseCase(protocolFactoryManager)
        createFileUseCase = CreateFileUseCase(protocolFactoryManager, getProtocolFromIdUseCase)
        getEnabledConnectionsUseCase = GetEnabledConnectionsUseCase(protocolFactoryManager)
        getFileStatUseCase = GetFileStatUseCase(protocolFactoryManager, getProtocolFromIdUseCase)
        listFilesInDirectoryUseCase =
            ListFilesInDirectoryUseCase(protocolFactoryManager, getProtocolFromIdUseCase)
        readFileUseCase = ReadFileUseCase(protocolFactoryManager, getProtocolFromIdUseCase)

        viewModel = CustomDocumentProviderViewModel(
            createFileUseCase,
            getEnabledConnectionsUseCase,
            getFileStatUseCase,
            listFilesInDirectoryUseCase,
            readFileUseCase
        )
    }

    /**
     * Only called when doing remote-to-remote copy. Remote-to-device and vice-versa do not trigger
     * this function.
     */
    override fun copyDocument(sourceDocumentId: String?, targetParentDocumentId: String?): String? {
        if (sourceDocumentId.isNullOrEmpty() or targetParentDocumentId.isNullOrEmpty()) {
            return null
        }

        return null
    }

    override fun createDocument(
        parentDocumentId: String?,
        mimeType: String?,
        displayName: String?
    ): String? {
        if (parentDocumentId.isNullOrEmpty() or displayName.isNullOrEmpty()) {
            return null
        }

        return viewModel.createDocument(
            parentDocumentId = parentDocumentId.orEmpty(),
            mimeType = mimeType.orEmpty(),
            displayName = displayName.orEmpty()
        )
    }

    override fun openDocument(
        documentId: String?, mode: String?, signal: CancellationSignal?
    ): ParcelFileDescriptor? {
        if (documentId == null) {
            return null
        }

        val (readPipe, writePipe) = ParcelFileDescriptor.createReliablePipe()

        viewModel.openDocument(documentId, writePipe)
        return readPipe
    }

    override fun queryChildDocuments(
        parentDocumentId: String?, projection: Array<out String?>?, sortOrder: String?
    ): Cursor {
        val column: Array<out String?> = projection ?: getDefaultDocumentProjection()
        val cursor = MatrixCursor(column)

        if (parentDocumentId != null) {
            viewModel.queryChildDocuments(cursor, parentDocumentId, sortOrder)
        }

        return cursor
    }

    override fun queryDocument(
        documentId: String?, projection: Array<out String?>?
    ): Cursor {
        val column: Array<out String?> = projection ?: getDefaultDocumentProjection()
        val cursor = MatrixCursor(column)

        if (documentId != null) {
            viewModel.queryDocument(cursor, documentId)
        }

        return cursor
    }

    override fun queryRoots(projection: Array<out String?>?): Cursor {
        val column: Array<out String?> = projection ?: getDefaultRootProjection()
        val cursor = MatrixCursor(column)

        viewModel.queryRoots(cursor)

        return cursor
    }

    override fun onCreate(): Boolean {
        val context = this.context ?: return false
        init(context)
        return true
    }

    fun getDefaultDocumentProjection(): Array<out String?> {
        val columnNames = listOf(
            DocumentsContract.Document.COLUMN_DOCUMENT_ID,
            DocumentsContract.Document.COLUMN_DISPLAY_NAME,
            DocumentsContract.Document.COLUMN_MIME_TYPE,
            DocumentsContract.Document.COLUMN_FLAGS,
            DocumentsContract.Document.COLUMN_SIZE,
            DocumentsContract.Document.COLUMN_LAST_MODIFIED,
        )
        return Array(columnNames.size) { index -> columnNames[index] }
    }

    fun getDefaultRootProjection(): Array<out String?> {
        val columnNames = listOf(
            DocumentsContract.Root.COLUMN_TITLE,
            DocumentsContract.Root.COLUMN_ROOT_ID,
            DocumentsContract.Root.COLUMN_FLAGS,
            DocumentsContract.Root.COLUMN_DOCUMENT_ID,
            DocumentsContract.Root.COLUMN_ICON
        )
        return Array(columnNames.size) { index -> columnNames[index] }
    }
}