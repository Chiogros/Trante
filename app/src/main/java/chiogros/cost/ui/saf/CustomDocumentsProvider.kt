package chiogros.cost.ui.saf

import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import android.provider.DocumentsProvider
import chiogros.cost.data.remote.repository.RemoteManager
import chiogros.cost.data.remote.sftp.LocalSftpDataSource
import chiogros.cost.data.remote.sftp.RemoteSftp
import chiogros.cost.data.remote.sftp.RemoteSftpDataSource
import chiogros.cost.data.remote.sftp.RemoteSftpRepository
import chiogros.cost.data.room.AppDatabase
import chiogros.cost.data.room.repository.ConnectionManager
import chiogros.cost.data.room.sftp.ConnectionSftpRepository
import chiogros.cost.data.room.sftp.ConnectionSftpRoomDataSource
import chiogros.cost.domain.CreateFileUseCase
import chiogros.cost.domain.GetEnabledConnectionsUseCase
import chiogros.cost.domain.GetFileStatUseCase
import chiogros.cost.domain.ListFilesInDirectoryUseCase
import chiogros.cost.domain.ReadFileUseCase
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
        // Room
        val connectionSftpRoomDataSource =
            ConnectionSftpRoomDataSource(AppDatabase.getDatabase(context).ConnectionSftpDao())
        val connectionSftpRepository = ConnectionSftpRepository(connectionSftpRoomDataSource)
        val connectionManager = ConnectionManager(connectionSftpRepository)

        // Remote
        val remoteSftp = RemoteSftp.new(dispatcher)
        val remoteSftpRoomDataSource = RemoteSftpDataSource(remoteSftp)
        val localSftpDataSource = LocalSftpDataSource()
        val remoteSftpRepository =
            RemoteSftpRepository(remoteSftpRoomDataSource, localSftpDataSource)
        val remoteManager = RemoteManager(remoteSftpRepository)

        // Domain layer
        createFileUseCase = CreateFileUseCase(connectionManager, remoteManager)
        getEnabledConnectionsUseCase = GetEnabledConnectionsUseCase(connectionManager)
        getFileStatUseCase = GetFileStatUseCase(connectionManager, remoteManager)
        listFilesInDirectoryUseCase = ListFilesInDirectoryUseCase(connectionManager, remoteManager)
        readFileUseCase = ReadFileUseCase(connectionManager, remoteManager)

        viewModel = CustomDocumentProviderViewModel(
            createFileUseCase = createFileUseCase,
            getEnabledConnectionsUseCase = getEnabledConnectionsUseCase,
            getFileStatUseCase = getFileStatUseCase,
            listFilesInDirectoryUseCase = listFilesInDirectoryUseCase,
            readFileUseCase = readFileUseCase
        )

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