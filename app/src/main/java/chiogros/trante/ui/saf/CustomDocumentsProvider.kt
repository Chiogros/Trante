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
import kotlinx.coroutines.Dispatchers

class CustomDocumentsProvider : DocumentsProvider() {
    lateinit var createFileUseCase: CreateFileUseCase
    lateinit var getEnabledConnectionsUseCase: GetEnabledConnectionsUseCase
    lateinit var getFileStatUseCase: GetFileStatUseCase
    lateinit var listFilesInDirectoryUseCase: ListFilesInDirectoryUseCase
    lateinit var readFileUseCase: ReadFileUseCase
    lateinit var viewModel: CustomDocumentProviderViewModel
    private val dispatcher = Dispatchers.IO

    fun init(context: Context): Boolean {
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
        val getProtocolFromIdUseCase = GetProtocolFromIdUseCase(protocolFactoryManager)
        createFileUseCase = CreateFileUseCase(protocolFactoryManager, getProtocolFromIdUseCase)
        getEnabledConnectionsUseCase = GetEnabledConnectionsUseCase(protocolFactoryManager)
        getFileStatUseCase = GetFileStatUseCase(protocolFactoryManager, getProtocolFromIdUseCase)
        listFilesInDirectoryUseCase =
            ListFilesInDirectoryUseCase(protocolFactoryManager, getProtocolFromIdUseCase)
        readFileUseCase = ReadFileUseCase(protocolFactoryManager, getProtocolFromIdUseCase)

        return true
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
    ): Cursor? {
        val column: Array<out String?> = projection ?: getDefaultDocumentProjection()
        val cursor = MatrixCursor(column)

        if (parentDocumentId != null) {
            viewModel.queryChildDocuments(cursor, parentDocumentId, sortOrder)
        }

        return cursor
    }

    override fun queryDocument(
        documentId: String?, projection: Array<out String?>?
    ): Cursor? {
        val column: Array<out String?>? = projection ?: getDefaultDocumentProjection()
        val cursor = MatrixCursor(column)

        if (documentId != null) {
            viewModel.queryDocument(cursor, documentId)
        }

        return cursor
    }

    override fun queryRoots(projection: Array<out String?>?): Cursor? {
        val column: Array<out String?>? = projection ?: getDefaultRootProjection()
        val cursor = MatrixCursor(column)

        viewModel.queryRoots(cursor)

        return cursor
    }

    override fun onCreate(): Boolean {
        val context = this.context
        if (context == null) {
            return false
        }

        return init(context)
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
        return Array(columnNames.size, { index -> columnNames[index] })
    }

    fun getDefaultRootProjection(): Array<out String?>? {
        val columnNames = listOf(
            DocumentsContract.Root.COLUMN_TITLE,
            DocumentsContract.Root.COLUMN_ROOT_ID,
            DocumentsContract.Root.COLUMN_FLAGS,
            DocumentsContract.Root.COLUMN_DOCUMENT_ID,
            DocumentsContract.Root.COLUMN_ICON
        )
        return Array(columnNames.size, { index -> columnNames[index] })
    }
}