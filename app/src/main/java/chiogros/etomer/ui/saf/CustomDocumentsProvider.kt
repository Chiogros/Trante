package chiogros.etomer.ui.saf

import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import android.provider.DocumentsProvider
import chiogros.etomer.R
import chiogros.etomer.data.remote.repository.RemoteManager
import chiogros.etomer.data.remote.sftp.RemoteSftp
import chiogros.etomer.data.remote.sftp.RemoteSftpDataSource
import chiogros.etomer.data.remote.sftp.RemoteSftpRepository
import chiogros.etomer.data.room.AppDatabase
import chiogros.etomer.data.room.repository.ConnectionManager
import chiogros.etomer.data.room.sftp.ConnectionSftpRepository
import chiogros.etomer.data.room.sftp.ConnectionSftpRoomDataSource
import chiogros.etomer.domain.GetEnabledConnectionsUseCase
import chiogros.etomer.domain.ListFilesInDirectoryUseCase
import chiogros.etomer.domain.ReadFileUseCase

class CustomDocumentsProvider : DocumentsProvider() {
    lateinit var listFilesInDirectoryUseCase: ListFilesInDirectoryUseCase
    lateinit var readFileUseCase: ReadFileUseCase
    lateinit var getEnabledConnectionsUseCase: GetEnabledConnectionsUseCase

    fun initUseCases(context: Context) {
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

        listFilesInDirectoryUseCase = ListFilesInDirectoryUseCase(connectionManager, remoteManager)
        readFileUseCase = ReadFileUseCase()
        getEnabledConnectionsUseCase = GetEnabledConnectionsUseCase(connectionManager)
    }

    override fun openDocument(
        documentId: String?,
        mode: String?,
        signal: CancellationSignal?
    ): ParcelFileDescriptor? {
        TODO("Not yet implemented")
    }

    override fun queryChildDocuments(
        parentDocumentId: String?,
        projection: Array<out String?>?,
        sortOrder: String?
    ): Cursor? {
        var column: Array<out String?>? = projection

        if (projection == null) {
            val columnNames = listOf<String>(
                DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                DocumentsContract.Document.COLUMN_MIME_TYPE,
                DocumentsContract.Document.COLUMN_FLAGS,
                DocumentsContract.Document.COLUMN_SIZE,
                DocumentsContract.Document.COLUMN_LAST_MODIFIED,
            )
            column = Array<String>(columnNames.size, { index -> columnNames[index] })
        }
        return MatrixCursor(column).apply {
            newRow().apply {
                add("a")
                add("a")
                add("a")
                add("a")
                add(64)
                add(1749484283000)
            }
            newRow().apply {
                add("b")
                add("b")
                add("b")
                add("b")
                add(64)
                add(1749484283000)
            }
        }
    }

    override fun queryDocument(
        documentId: String?,
        projection: Array<out String?>?
    ): Cursor? {
        var column: Array<out String?>? = projection

        if (projection == null) {
            val columnNames = listOf<String>(
                DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                DocumentsContract.Document.COLUMN_DISPLAY_NAME,
                DocumentsContract.Document.COLUMN_MIME_TYPE,
                DocumentsContract.Document.COLUMN_FLAGS,
                DocumentsContract.Document.COLUMN_SIZE,
                DocumentsContract.Document.COLUMN_LAST_MODIFIED,
            )
            column = Array<String>(columnNames.size, { index -> columnNames[index] })
        }

        val arr = MatrixCursor(column).apply {
            newRow().apply {
                add(DocumentsContract.Document.COLUMN_DOCUMENT_ID, "root")
                add(DocumentsContract.Document.COLUMN_DISPLAY_NAME, "root")
                add(
                    DocumentsContract.Document.COLUMN_MIME_TYPE,
                    DocumentsContract.Document.MIME_TYPE_DIR
                )
                add(DocumentsContract.Document.COLUMN_SIZE, null)
                add(DocumentsContract.Document.COLUMN_LAST_MODIFIED, null)
            }
        }

        return arr
    }

    override fun queryRoots(projection: Array<out String?>?): Cursor? {
        val context = this.context
        if (context == null) {
            error(R.string.no_context)
        }

        var column: Array<out String?>? = projection

        if (projection == null) {
            val columnNames = listOf<String>(
                DocumentsContract.Root.COLUMN_TITLE,
                DocumentsContract.Root.COLUMN_ROOT_ID,
                DocumentsContract.Root.COLUMN_FLAGS,
                DocumentsContract.Root.COLUMN_DOCUMENT_ID,
                DocumentsContract.Root.COLUMN_ICON
            )
            column = Array(columnNames.size, { index -> columnNames[index] })
        }

        val cursor = MatrixCursor(column)

        // It's possible to have multiple roots (e.g. for multiple accounts in the
        // same app) -- just add multiple cursor rows.
        getEnabledConnectionsUseCase().forEach { con ->
            cursor.newRow().apply {
                // Set SAF entry with connection name, or user@host otherwise
                add(
                    DocumentsContract.Root.COLUMN_TITLE,
                    con.name.ifEmpty { con.user + "@" + con.host }
                )
                add(DocumentsContract.Root.COLUMN_ICON, R.drawable.ic_launcher)
                add(DocumentsContract.Root.COLUMN_ROOT_ID, con.id)
                add(DocumentsContract.Root.COLUMN_DOCUMENT_ID, con.id + "/")
                add(
                    DocumentsContract.Root.COLUMN_FLAGS,
                    DocumentsContract.Root.FLAG_SUPPORTS_CREATE
                            or DocumentsContract.Root.FLAG_SUPPORTS_SEARCH
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
        initUseCases(context)

        return true
    }
}