package chiogros.cost.ui.saf

import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import android.provider.DocumentsProvider
import android.webkit.MimeTypeMap
import chiogros.cost.R
import chiogros.cost.data.remote.File
import chiogros.cost.data.remote.FileAttributesType
import chiogros.cost.data.remote.repository.RemoteManager
import chiogros.cost.data.remote.sftp.RemoteSftp
import chiogros.cost.data.remote.sftp.RemoteSftpDataSource
import chiogros.cost.data.remote.sftp.RemoteSftpRepository
import chiogros.cost.data.room.AppDatabase
import chiogros.cost.data.room.repository.ConnectionManager
import chiogros.cost.data.room.sftp.ConnectionSftpRepository
import chiogros.cost.data.room.sftp.ConnectionSftpRoomDataSource
import chiogros.cost.domain.GetEnabledConnectionsUseCase
import chiogros.cost.domain.ListFilesInDirectoryUseCase
import chiogros.cost.domain.ReadFileUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CustomDocumentsProvider : DocumentsProvider() {
    lateinit var listFilesInDirectoryUseCase: ListFilesInDirectoryUseCase
    lateinit var readFileUseCase: ReadFileUseCase
    lateinit var getEnabledConnectionsUseCase: GetEnabledConnectionsUseCase
    private val providerScope = CoroutineScope(Dispatchers.IO)

    fun initUseCases(context: Context): Boolean {
        // Room
        val connectionSftpRoomDataSource =
            ConnectionSftpRoomDataSource(AppDatabase.getDatabase(context).ConnectionSftpDao())
        val connectionSftpRepository = ConnectionSftpRepository(connectionSftpRoomDataSource)
        val connectionManager = ConnectionManager(connectionSftpRepository)

        // Remote
        val remoteSftp = RemoteSftp()
        val remoteSftpRoomDataSource = RemoteSftpDataSource(remoteSftp)
        val remoteSftpRepository = RemoteSftpRepository(remoteSftpRoomDataSource)
        val remoteManager = RemoteManager(remoteSftpRepository)

        // Domain layer
        listFilesInDirectoryUseCase = ListFilesInDirectoryUseCase(connectionManager, remoteManager)
        readFileUseCase = ReadFileUseCase(connectionManager, remoteManager)
        getEnabledConnectionsUseCase = GetEnabledConnectionsUseCase(connectionManager)

        return true
    }

    override fun openDocument(
        documentId: String?, mode: String?, signal: CancellationSignal?
    ): ParcelFileDescriptor? {
        if (documentId == null) {
            return null
        }

        val conId = getConnectionIdFromDocumentId(documentId)
        val path = getPathFromDocumentId(documentId)

        val (outPipe, inPipe) = ParcelFileDescriptor.createReliablePipe()

        providerScope.launch {
            ParcelFileDescriptor.AutoCloseOutputStream(inPipe).use { output ->
                // Read remote file then hand it into the pipe
                output.write(readFileUseCase(conId, path))
            }
        }

        return outPipe
    }

    override fun queryChildDocuments(
        parentDocumentId: String?, projection: Array<out String?>?, sortOrder: String?
    ): Cursor? {
        val column: Array<out String?>? = projection ?: getDefaultDocumentProjection()
        val cursor = MatrixCursor(column)

        if (parentDocumentId == null) {
            return cursor
        }

        val conId = getConnectionIdFromDocumentId(parentDocumentId)
        val path = getPathFromDocumentId(parentDocumentId)

        var files: List<File> = emptyList()
        runBlocking {
            files = listFilesInDirectoryUseCase(conId, path)
        }

        filterNavigationFiles(files).forEach { file ->
            cursor.newRow().apply {
                add(
                    DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                    parentDocumentId + "/" + file.path.fileName.toString()
                )
                add(
                    DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                    file.path.fileName.toString()
                )
                add(DocumentsContract.Document.COLUMN_MIME_TYPE, getMimetypeFromFile(file))
                add(DocumentsContract.Document.COLUMN_FLAGS, 0)
                add(DocumentsContract.Document.COLUMN_SIZE, file.size)
                add(DocumentsContract.Document.COLUMN_LAST_MODIFIED, null)
            }
        }

        return cursor
    }

    /**
     * It seems to be mostly called to get data about root folder,
     * which is the reason why "." folder is hardcoded.
     */
    override fun queryDocument(
        documentId: String?, projection: Array<out String?>?
    ): Cursor? {
        val column: Array<out String?>? = projection ?: getDefaultDocumentProjection()
        val cursor = MatrixCursor(column)

        if (documentId == null) {
            return cursor
        }

        cursor.newRow().apply {
            add(DocumentsContract.Document.COLUMN_DOCUMENT_ID, documentId)
            add(DocumentsContract.Document.COLUMN_DISPLAY_NAME, ".")
            add(
                DocumentsContract.Document.COLUMN_MIME_TYPE,
                DocumentsContract.Document.MIME_TYPE_DIR
            )
            add(DocumentsContract.Document.COLUMN_FLAGS, 0)
            add(DocumentsContract.Document.COLUMN_SIZE, null)
            add(DocumentsContract.Document.COLUMN_LAST_MODIFIED, null)
        }

        return cursor
    }

    override fun queryRoots(projection: Array<out String?>?): Cursor? {
        val context = this.context
        if (context == null) {
            error(R.string.no_context)
        }

        val column: Array<out String?>? = projection ?: getDefaultRootProjection()
        val cursor = MatrixCursor(column)

        // It's possible to have multiple roots (e.g. for multiple accounts in the
        // same app) -- just add multiple cursor rows.
        getEnabledConnectionsUseCase().forEach { con ->
            cursor.newRow().apply {
                add(DocumentsContract.Root.COLUMN_ROOT_ID, con.id)
                // Set SAF entry with connection name, or user@host otherwise
                add(
                    DocumentsContract.Root.COLUMN_TITLE,
                    con.name.ifEmpty { getConnectionFriendlyName(con.user, con.host) })
                add(DocumentsContract.Root.COLUMN_ICON, R.drawable.ic_launcher)
                add(DocumentsContract.Root.COLUMN_DOCUMENT_ID, con.id)
                add(
                    DocumentsContract.Root.COLUMN_FLAGS,
                    DocumentsContract.Root.FLAG_SUPPORTS_CREATE or DocumentsContract.Root.FLAG_SUPPORTS_SEARCH
                )
            }
        }

        return cursor
    }

    override fun onCreate(): Boolean {
        val context = this.context
        if (context == null) {
            error(R.string.no_context)
        }

        return initUseCases(context)
    }

    fun getDefaultDocumentProjection(): Array<out String?>? {
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

    fun getConnectionFriendlyName(user: String, host: String): String {
        return "$user@$host"
    }

    fun getConnectionIdFromDocumentId(documentId: String): String {
        return documentId.substringBefore('/')
    }

    fun getPathFromDocumentId(documentId: String): String {
        return documentId.substringAfter('/', ".")
    }

    fun getMimetypeFromFile(file: File): String {
        return when (file.type) {
            FileAttributesType.DIRECTORY -> DocumentsContract.Document.MIME_TYPE_DIR
            FileAttributesType.SYMLINK -> DocumentsContract.Document.MIME_TYPE_DIR
            FileAttributesType.REGULAR -> getMimetypeFromFilename(file.path.fileName.toString())
            FileAttributesType.UNKNOWN -> "application/octet-stream"
        }
    }

    fun getMimetypeFromFilename(filename: String): String {
        // Resolve MIME type based on filename extension
        val resolved = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
            filename.substringAfterLast('.')
        )

        // Default MIME type if filename couldn't be used to resolve it
        return resolved ?: "application/octet-stream"
    }

    // Do not list . and .. directories
    fun filterNavigationFiles(files: List<File>): List<File> {
        // Match . and ..
        val hideDirectoriesRegex = Regex("^\\.{1,2}$")

        return files.filter { it.path.fileName.toString().matches(hideDirectoriesRegex).not() }
    }
}