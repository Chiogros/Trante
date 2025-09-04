package chiogros.cost.ui.saf

import android.database.MatrixCursor
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import android.webkit.MimeTypeMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chiogros.cost.R
import chiogros.cost.data.remote.File
import chiogros.cost.data.remote.FileAttributesType
import chiogros.cost.domain.GetEnabledConnectionsUseCase
import chiogros.cost.domain.GetFileStatUseCase
import chiogros.cost.domain.ListFilesInDirectoryUseCase
import chiogros.cost.domain.ReadFileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.InputStream
import kotlin.io.path.Path

data class CustomDocumentProviderUiState(
    val userConnected: Boolean = false
)

class CustomDocumentProviderViewModel(
    val getEnabledConnectionsUseCase: GetEnabledConnectionsUseCase,
    val getFileStatUseCase: GetFileStatUseCase,
    val listFilesInDirectoryUseCase: ListFilesInDirectoryUseCase,
    val readFileUseCase: ReadFileUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CustomDocumentProviderUiState())
    val uiState: StateFlow<CustomDocumentProviderUiState> = _uiState.asStateFlow()

    fun openDocument(documentId: String, writePipe: ParcelFileDescriptor) {
        val conId = getConnectionIdFromDocumentId(documentId)
        val path = getPathFromDocumentId(documentId)

        var fileStream: InputStream

        // Read remote file then hand it into the pipe
        viewModelScope.launch {
            fileStream = readFileUseCase(conId, path)
            val bufSize = 4096
            val buff = ByteArray(bufSize)
            var bytesRead: Int

            ParcelFileDescriptor.AutoCloseOutputStream(writePipe).use { output ->
                do {
                    withContext(Dispatchers.IO) {
                        bytesRead = fileStream.read(buff, 0, bufSize)
                    }
                    output.write(buff)
                } while (bytesRead == bufSize)
            }

            withContext(Dispatchers.IO) {
                fileStream.close()
            }
        }
    }

    fun queryChildDocuments(
        cursor: MatrixCursor, parentDocumentId: String, sortOrder: String?
    ) {
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
                    DocumentsContract.Document.COLUMN_DISPLAY_NAME, file.path.fileName.toString()
                )
                add(DocumentsContract.Document.COLUMN_MIME_TYPE, getMimetypeFromFile(file))
                add(DocumentsContract.Document.COLUMN_FLAGS, 0)
                add(DocumentsContract.Document.COLUMN_SIZE, file.size)
                add(DocumentsContract.Document.COLUMN_LAST_MODIFIED, null)
            }
        }
    }

    fun queryDocument(
        cursor: MatrixCursor, documentId: String
    ) {
        val conId = getConnectionIdFromDocumentId(documentId)
        val path = getPathFromDocumentId(documentId)

        var file = File(Path(""))
        runBlocking {
            file = getFileStatUseCase(conId, path)
        }

        cursor.newRow().apply {
            add(DocumentsContract.Document.COLUMN_DOCUMENT_ID, documentId)
            add(DocumentsContract.Document.COLUMN_DISPLAY_NAME, path)
            add(DocumentsContract.Document.COLUMN_MIME_TYPE, getMimetypeFromFile(file))
            add(DocumentsContract.Document.COLUMN_FLAGS, 0)
            add(DocumentsContract.Document.COLUMN_SIZE, file.size)
            add(DocumentsContract.Document.COLUMN_LAST_MODIFIED, null)
        }
    }

    fun queryRoots(cursor: MatrixCursor) {
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
    }

    // Do not list . and .. directories
    fun filterNavigationFiles(files: List<File>): List<File> {
        // Match . and ..
        val hideDirectoriesRegex = Regex("^\\.{1,2}$")

        return files.filter { it.path.fileName.toString().matches(hideDirectoriesRegex).not() }
    }

    fun getConnectionFriendlyName(user: String, host: String): String {
        return "$user@$host"
    }

    fun getConnectionIdFromDocumentId(documentId: String): String {
        return documentId.substringBefore('/')
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

    fun getPathFromDocumentId(documentId: String): String {
        return documentId.substringAfter('/', ".")
    }
}