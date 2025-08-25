package chiogros.cost.ui.saf

import android.os.ParcelFileDescriptor
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chiogros.cost.domain.ListFilesInDirectoryUseCase
import chiogros.cost.domain.ReadFileUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CustomDocumentProviderUiState(

)

class CustomDocumentProviderViewModel(
    listFilesInDirectoryUseCase: ListFilesInDirectoryUseCase,
    readFileUseCase: ReadFileUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CustomDocumentProviderUiState())
    val uiState: StateFlow<CustomDocumentProviderUiState> = _uiState.asStateFlow()

    fun getFilesList(
        a: ParcelFileDescriptor.AutoCloseOutputStream,
        b: ParcelFileDescriptor.AutoCloseInputStream
    ) {
        viewModelScope.launch {
            delay(5000)
            a.close()
            b.close()
        }
    }
}