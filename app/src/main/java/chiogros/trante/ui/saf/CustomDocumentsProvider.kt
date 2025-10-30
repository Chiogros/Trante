package chiogros.trante.ui.saf

import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import android.provider.DocumentsProvider
import chiogros.trante.data.network.repository.NetworkManager
import chiogros.trante.data.network.sftp.LocalSftpNetworkDataSource
import chiogros.trante.data.network.sftp.RemoteSftpNetworkDataSource
import chiogros.trante.data.network.sftp.SftpNetwork
import chiogros.trante.data.network.sftp.SftpNetworkRepository
import chiogros.trante.data.room.AppDatabase
import chiogros.trante.data.room.repository.RoomManager
import chiogros.trante.data.room.sftp.SftpRoomDataSource
import chiogros.trante.data.room.sftp.SftpRoomRepository
import chiogros.trante.domain.CreateFileUseCase
import chiogros.trante.domain.GetEnabledConnectionsUseCase
import chiogros.trante.domain.GetFileStatUseCase
import chiogros.trante.domain.ListFilesInDirectoryUseCase
import chiogros.trante.domain.ReadFileUseCase
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
        val sftpRoomDataSource =
            SftpRoomDataSource(AppDatabase.getDatabase(context).connectionSftpDao())
        val sftpRoomRepository = SftpRoomRepository(sftpRoomDataSource)
        val roomManager = RoomManager(sftpRoomRepository)
        // Remote
        val sftpNetwork = SftpNetwork.new(dispatcher)
        val remoteSftpRoomDataSource = RemoteSftpNetworkDataSource(sftpNetwork)
        val localSftpNetworkDataSource = LocalSftpNetworkDataSource()
        val sftpNetworkRepository =
            SftpNetworkRepository(remoteSftpRoomDataSource, localSftpNetworkDataSource)
        val networkManager = NetworkManager(sftpNetworkRepository)
        // Domain layer
        createFileUseCase = CreateFileUseCase(roomManager, networkManager)
        getEnabledConnectionsUseCase = GetEnabledConnectionsUseCase(roomManager)
        getFileStatUseCase = GetFileStatUseCase(roomManager, networkManager)
        listFilesInDirectoryUseCase = ListFilesInDirectoryUseCase(roomManager, networkManager)
        readFileUseCase = ReadFileUseCase(roomManager, networkManager)

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