package chiogros.cost.ui.saf

import android.os.ParcelFileDescriptor
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chiogros.cost.domain.ListFilesInDirectoryUseCase
import chiogros.cost.domain.ReadFileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream

data class CustomDocumentProviderUiState(
    val userConnected: Boolean = false
)

class CustomDocumentProviderViewModel(
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

    fun getConnectionIdFromDocumentId(documentId: String): String {
        return documentId.substringBefore('/')
    }

    fun getPathFromDocumentId(documentId: String): String {
        return documentId.substringAfter('/', ".")
    }
}